package com.beyond.basic.b2_board.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenFilter extends GenericFilter {
    @Value("${jwt.secretKeyAt}")
    private String secretKeyAt;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 토큰이 있는지 없는 지 검사

        HttpServletRequest req = (HttpServletRequest) request;
        String bearerToken = req.getHeader("Authorization"); // Header의 Authorization에 토큰을 달고 오기때문에

        if (bearerToken == null) {
//            token이 없는 경우 다시 filterChain으로 되돌아 가는 로직
            chain.doFilter(request, response);
            return;
        }

        // token이 있는 경우 bearerToken 검증 후 Authentication 객체 생성

        String token = bearerToken.substring(7); // "Bearer "를 뜯어낸다
        // token "검증" 및 "claims(페이로드(사용자정보))" 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyAt)
                .build()
                .parseClaimsJws(token)
                .getBody();

//        claims.getSubject();
//        claims.get("role");

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role"))); // Authentication객체를 만들 때 권한은 ROLE_ 라는 키워드를 붙여서 만들어 주는 것이 추후 문제 발생 X

        Authentication authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
