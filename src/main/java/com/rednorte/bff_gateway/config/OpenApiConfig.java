package com.rednorte.bff_gateway.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    /**
     * Definir los metoods principales de la API documentada
     * @return instancia configurada
     */

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BFF Gateway - RedNorte")
                        .version("1.0.0")
                        .description("API gateway unificado para el sistema de reasignacion de citas medicas")
                        .contact(new Contact()
                                .name("Equipo RedNorte")
                                .email("contacto@rednorte.cl")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor local")
                ));
    }
}
