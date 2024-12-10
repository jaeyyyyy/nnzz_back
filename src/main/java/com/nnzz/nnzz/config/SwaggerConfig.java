package com.nnzz.nnzz.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
        return new OpenAPI()
                .addServersItem(new Server().url(serverUrl).description("서버 URL"))
                .info(new Info().title("nnzz API")
                        .description("냠냠쩝쩝 프로젝트 API 명세서입니다.")
                        .version("v0.0.1"));

    }
}
