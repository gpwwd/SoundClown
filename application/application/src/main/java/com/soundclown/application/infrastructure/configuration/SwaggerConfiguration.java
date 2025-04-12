package com.soundclown.application.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Swagger/OpenAPI для документации REST API.
 * <p>
 * Настраивает заголовки безопасности (JWT), общую информацию о сервисе и схему авторизации для Swagger UI.
 */
@Configuration
public class SwaggerConfiguration {

    /**
     * Создаёт и настраивает OpenAPI-документацию для Swagger UI.
     * <p>
     * Добавляет информацию о сервисе, версию, описание и схему авторизации через JWT Bearer Token.
     *
     * @return объект {@link OpenAPI}, содержащий описание и конфигурацию API
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SoundClown API")
                        .version("1.0")
                        .description("API Documentation with JWT Authentication"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
