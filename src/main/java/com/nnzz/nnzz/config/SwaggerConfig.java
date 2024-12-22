package com.nnzz.nnzz.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Value("${server.url}")
    private String serverUrl;

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("nnzz")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI springOpenAPI() {
        // securitySecheme 명
        String schemeName = "Authorization";

        // API 요청헤더에 인증정보 포함
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(schemeName);
        // SecuritySchemes 등록
        Components components = new Components().addSecuritySchemes(schemeName, new SecurityScheme()
                .name(schemeName)
                .type(SecurityScheme.Type.HTTP) // HTTP 방식
                .scheme("bearer")
                .bearerFormat("JWT")); // 토큰 형식을 지정하는 임의의 문자(optional)

        return new OpenAPI()
                .components(new Components())
                .addSecurityItem(securityRequirement)
                .addServersItem(new Server().url(serverUrl).description("서버 URL"))
                .info(new Info().title("nnzz API")
                        .description("냠냠쩝쩝 프로젝트 API 명세서입니다.")
                        .version("v0.0.1"));

    }
}
