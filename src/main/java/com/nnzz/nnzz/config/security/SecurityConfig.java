package com.nnzz.nnzz.config.security;


import com.nnzz.nnzz.config.jwt.JwtAuthFilter;
import com.nnzz.nnzz.config.jwt.JwtTokenProvider;
import com.nnzz.nnzz.exception.CustomAuthEntryPoint;
// import com.nnzz.nnzz.oauth.CustomAuthenticationProvider;
import com.nnzz.nnzz.exception.RequestURIFilter;
import com.nnzz.nnzz.oauth.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtTokenProvider jwtTokenProvider, CustomAuthEntryPoint customAuthEntryPoint, AuthenticationManagerBuilder auth, RequestURIFilter requestURIFilter) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
        http.httpBasic(HttpBasicConfigurer::disable) // HTTP 기본 인증 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // httpBasic 인증 방식 사용 x
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthEntryPoint)) // 토큰 오류 핸들링

                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))

                // Jwt 로그인
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll() // 모든 OPTIONS 요청 허용
                        .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(requestURIFilter, JwtAuthFilter.class)
                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);



        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000", "https://localhost:3000", "https://www.nnzz.today"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Headers",
                "Access-Control-Request-Method", "Access-Control-Request-Headers",
                "X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"));
        config.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        config.setMaxAge(3600L); // 1시간

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Bean
//    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
//        return authenticationManagerBuilder.build();
//    }

//    @Bean
//    public AuthenticationManagerBuilder configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(customAuthenticationProvider);
//        return auth;
//    }
}
