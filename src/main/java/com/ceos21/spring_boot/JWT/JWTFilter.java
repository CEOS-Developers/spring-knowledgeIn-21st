package com.ceos21.spring_boot.JWT;

import com.ceos21.spring_boot.DTO.auth.CustomUserDetailsDTO;
import com.ceos21.spring_boot.Domain.user.User;
import com.ceos21.spring_boot.Repository.user.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;


// API 요청마다 실행되는 JWT 인증 필터
// Access 토큰이 존재하면 해당 토큰을 검증하고, 인증 객체를 SecurityContext에 등록
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;  // DB에서 사용자 정보 조회용

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 요청 헤더에서 Access 토큰 가져오기
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = authHeader.substring(7); // "Bearer " 이후 토큰만 추출


        // 토큰 없으면 다음 필터로 넘어가기
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 검증 (서명 확인, 형식 확인 등)
        try {
            jwtUtil.verifyToken(accessToken);
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우 401 응답 반환
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");
            return;
        } catch (Exception e) {
            // 그 외 유효하지 않은 토큰
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter writer = response.getWriter();
            writer.print("invalid token");
            return;
        }

        // 해당 토큰이 Access 토큰인지 확인
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");
            return;
        }

        // 유효한 토큰일 경우 사용자 정보 추출
        String email = jwtUtil.getEmail(accessToken);

        // DB에서 사용자 정보 조회 (실제 존재하는지 검증)
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // User 정보 기반으로 UserDetails 생성
        CustomUserDetailsDTO customUserDetailsDTO = new CustomUserDetailsDTO(user);

        // Authentication 객체 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                customUserDetailsDTO, null, customUserDetailsDTO.getAuthorities()
        );

        // 인증 객체를 SecurityContextHolder에 저장 -> 이후 인증 완료 상태로 요청 처리됨
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}

