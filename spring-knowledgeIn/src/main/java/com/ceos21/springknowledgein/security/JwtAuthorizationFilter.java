package com.ceos21.springknowledgein.security;

import com.ceos21.springknowledgein.security.user.CustomUserDetails;
import com.ceos21.springknowledgein.security.user.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    //OncePerRequestFilter는 다음에 등록된 필터로 요청을 넘겨야 하기 떄문에 doFilterInternal이 필요함.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = jwtUtil.getAccessTokenFromHeader(request);

        if(StringUtils.hasText(accessToken) && jwtUtil.validateToken(accessToken)) {
            Claims claims = jwtUtil.getUserInfoFromToken(accessToken);
            String username = claims.getSubject();

            //()는 명시적 타입 캐스팅
            CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }


}
