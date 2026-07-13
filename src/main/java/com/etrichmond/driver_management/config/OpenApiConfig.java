package com.etrichmond.driver_management.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI transportOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Transport Management API")
                        .description("Driver and Vehicle Management System")
                        .version("v1.0"));
    }
}