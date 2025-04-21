package com.rebels.quasar.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author FDip
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI quasarFireOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Operación Fuego de Quasar API")
                        .description("API para triangulación de posición y decodificación de mensajes de naves rebeldes")
                        .version("1.0.0"));
    }
}