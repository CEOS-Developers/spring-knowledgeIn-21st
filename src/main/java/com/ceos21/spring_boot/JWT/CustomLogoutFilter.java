package com.ceos21.spring_boot.JWT;

import com.ceos21.spring_boot.Repository.auth.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        // 로그아웃 경로가 아니거나 POST가 아닌 요청은 다음 필터로 넘김
        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();
        if (!requestUri.equals("/logout") || !requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키에서 Refresh 토큰 추출
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }

        // Refresh 토큰이 없는 경우 → 400 응답
        if (refresh == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Refresh token is missing.");
            return;
        }

        // Refresh 토큰 검증
        try {
            jwtUtil.verifyToken(refresh); // 서명 + 만료 검증
        } catch (ExpiredJwtException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Refresh token expired.");
            return;
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid refresh token.");
            return;
        }

        // 카테고리가 refresh인지 확인
        String category = jwtUtil.getCategory(refresh);
        if (!"refresh".equals(category)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not a refresh token.");
            return;
        }

        // Refresh 토큰이 DB에 존재하는지 확인
        if (!refreshRepository.existsByRefresh(refresh)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Refresh token not found in DB.");
            return;
        }

        // DB에서 Refresh 토큰 삭제
        refreshRepository.deleteByRefresh(refresh);

        // 클라이언트의 쿠키 삭제
        Cookie expiredCookie = new Cookie("refresh", null);
        expiredCookie.setMaxAge(0);
        expiredCookie.setPath("/");
        response.addCookie(expiredCookie);

        // 성공 응답
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
