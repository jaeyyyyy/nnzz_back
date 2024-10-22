package com.nnzz.nnzz.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("nnzz")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("nnzz API")
                        .description("냠냠쩝쩝 프로젝트 API 명세서입니다.")
                        .version("v0.0.1"));

    }
}
