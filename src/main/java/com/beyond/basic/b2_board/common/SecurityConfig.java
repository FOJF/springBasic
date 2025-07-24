package com.beyond.basic.b2_board.common;

import com.beyond.basic.b2_board.common.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final JwtAuthenticationHandler jwtAuthenticationHandler;
    private final JwtAuthorizationHandler jwtAuthorizationHandler;

    // 내가 만든 클래스의 싱글톤화는 Component
    // 외부 라이브러리를 활용한 싱글톤은 Bean + Configuration
    @Bean // 리턴한 SecurityFilterChain 객체를 싱글톤으로 만든다
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // filter계층에서 filter로직을 커스텀하기 위함
        return httpSecurity
                .cors(c -> c.configurationSource(corsConfigurationSource())) // 특정도메인에 대한 허용정책, postman은 cors정책에 적용X(도메인이 없기 때문)
                // csrf(보안공격 중 하나로서 타 사이트의 쿠키 값을 꺼내서 탈취하는 공격) 비활성화
                // 세션기반 로그인(mvc, ssr[타임리프, jsp])에서는 csrf별도 설정하는 것이 일반적
                // 토큰기반 로그인(rest api 서버, csr)에서는 csrf별도 설정하지 않는 것이 일반적
                .csrf(AbstractHttpConfigurer::disable)
                // http basic은 email/pw를 인코딩하여 인증하는 방식(암호화가 아니라 인코딩이기때문에 보안에 취약, 잘 사용되지 않음)
                .httpBasic(AbstractHttpConfigurer::disable)
                // 세션로그인 방식 비활성화
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // jwtTokenFilter 토큰을 검증하고, Authentication 객체 생성
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class) //// 무조건 여기서 필터링 (근데 로그인, 회원가입은 토큰을 가지고 올 수가 없음, 토큰을 받으려고 하는 거니까 -> 그래서 하위에 예외api로 등록해줘서 통과시켜줌)
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(jwtAuthenticationHandler) // 401
                                .accessDeniedHandler(jwtAuthorizationHandler) // 403
                )
                // 예외 api 정책 설정
                // authenticated() : 예외를 제외한 모든 요청에 대해서 Authentication객체가 생성되기를 요구
                .authorizeHttpRequests(a -> a.requestMatchers("/author/create", "author/doLogin").permitAll().anyRequest().authenticated())
                .build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 적혀있는 url에 대해서 허용해주겠다
        configuration.setAllowedMethods(List.of("*")); // 모든 HTTP(get, post 등) 메서드 허용
        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더요소(Authorization 등) 허용
        configuration.setAllowCredentials(true); // 자격 증명 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); //모든 url패턴에 대해 cors설정 적용 (추가적인 url, /author, /author/detail/1등등)
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
