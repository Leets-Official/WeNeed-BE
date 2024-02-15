package org.example.weneedbe.global.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("JWT");
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("JWT", securityScheme))
                .security(Arrays.asList(securityRequirement))
                .addServersItem(new Server().url("/"))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
            .title("WeNeed API")
            .description("WeNeed : API 명세서")
            .version("v1.0.0");
    }
}