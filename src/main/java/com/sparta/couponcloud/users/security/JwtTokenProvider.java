package com.sparta.couponcloud.users.security;


import com.sparta.couponcloud.config.JwtConfig;
import com.sparta.couponcloud.users.entity.Role;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.secretKey = jwtConfig.getSecretKey();
        this.accessTokenExpiration = jwtConfig.getAccessTokenExpiration();
        this.refreshTokenExpiration = jwtConfig.getRefreshTokenExpiration();
    }

    public String generateAccessToken(int userId, Role role) {
        return Jwts.builder()
                .subject("access-token")
                .claim("userId", userId)
                .claim("role", role.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(int userId, Role role) {
        return Jwts.builder()
                .subject("refresh-token")
                .claim("userId", userId)
                .claim("role", role.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        log.info("Validating Token: {}", token);
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith(secretKey)
                    .build();

            parser.parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("토큰검증 실패" + e.getMessage());
            return false;
        }
    }

    public Claims getClaims(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith(secretKey)
                    .build();

            Jws<Claims> jws = parser.parseSignedClaims(token);
            return jws.getPayload();
        } catch (Exception e) {
            log.error("클레임 파싱 실패" + e.getMessage());
            throw new IllegalArgumentException("유효하지 않은 토큰입니다");
        }
    }

    // role 권한 배출
    public List<GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    public long getRefreshTokenExpiration() {
        return this.refreshTokenExpiration;
    }
}
