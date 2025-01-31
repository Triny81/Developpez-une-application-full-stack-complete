package com.openclassrooms.mddapi.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringFoxConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("MDD API").version("1.0").description("Documentation de l'API"))
                .addSecurityItem(new SecurityRequirement().addList("JWT Cookie"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("JWT Cookie", new SecurityScheme()
                                .name("JWT Cookie")
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)));
    }
}
