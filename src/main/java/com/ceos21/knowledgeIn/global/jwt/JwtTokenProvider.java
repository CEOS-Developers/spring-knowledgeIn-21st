package com.ceos21.knowledgeIn.global.jwt;

import com.ceos21.knowledgeIn.domain.member.dto.JwtTokenDTO;
import com.ceos21.knowledgeIn.domain.member.service.CustomUserDetailService;
import com.ceos21.knowledgeIn.global.util.StaticValues;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
/**
 * 토큰 생성, 토큰 복호화, 토큰 유효성 검사 등 토큰 관련 메서드 구현
 */
public class JwtTokenProvider {
    private final Key key;
    private final CustomUserDetailService customUserDetailService;

    //application.yml 에서 값을 가져옴(yml은 깃허브에 업로드 X)
    public JwtTokenProvider(@Value("${jwt.secret}") String key, CustomUserDetailService customUserDetailService) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.customUserDetailService = customUserDetailService;
    }

    //멤버 정보를 바탕으로 Refresh,Access 토큰 생성
    public JwtTokenDTO generateToken(Authentication authentication) {
        //권한 가져오기
        String auth = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        //AccessToken 생성
        Date accessTokenExpiresIn = new Date(now + StaticValues.JWT_ACCESS_TOKEN_VALID_TIME);
        String accessToken = Jwts.builder()
                .setIssuedAt(new Date(now))
                .setSubject(authentication.getName())
                .claim("auth", auth)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        //RefreshToken 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + StaticValues.JWT_REFRESH_TOKEN_VALID_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtTokenDTO.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    //토큰이 유효한지 검사(만료 시간, 블랙리스트 여부(->stateless하지 않으므로 비권장) 등)
    public boolean validateToken(String token) {

        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }
        //예외 처리
        catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }

        //블랙리스트는 구현 X

        return false;
    }


    //토큰을 복호화하여 안에 있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        String userPk = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();
        //토큰에서 이메일을 꺼내어 UserDetails 형의 멤버 객체를 반환받는다.
        UserDetails userDetails = customUserDetailService.loadUserByUsername(userPk);

        //반환된 객체를 바탕으로 인증 객체를 생성한다.
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
