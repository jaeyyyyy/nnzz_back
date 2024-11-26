package com.nnzz.nnzz.config.security;

import com.nnzz.nnzz.service.OauthUserService;
import groovy.util.logging.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, OauthUserService oauthUserService) throws Exception {
        http.httpBasic(HttpBasicConfigurer::disable) // HTTP 기본 인증 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // httpBasic 인증 방식 사용 x
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Arrays.asList("Authorization", "Authorization-refresh", "Cache-Control", "Content-Type"));
                    config.setExposedHeaders(Arrays.asList("Authorization", "Authorization-refresh"));
                    config.setMaxAge(3600L); // 1시간
                    return config;
                })) // cors
                ;


        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/**", "/auth/login/**").permitAll()
                .anyRequest().authenticated());

        // http.oauth2Login(Customizer.withDefaults());
        http.oauth2Login((oauth2Login) -> oauth2Login
                .loginProcessingUrl("/auth/login/**")
//                .userInfoEndpoint(userInfo -> userInfo.userService(oauthUserService))
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}