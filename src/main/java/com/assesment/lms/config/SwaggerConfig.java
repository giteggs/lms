package com.assesment.lms.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("library-management-system")
                .pathsToMatch("/api/v1/lms/books/**", "/api/v1/lms/borrowers/**")
                .build();
    }

    @Bean
    public OpenAPI lmsAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Library Management System API")
                        .version("1.0")
                        .description("API documentation for the Library Management System")
                        .contact(new Contact().name("Your Name").email("your.email@example.com"))
                );
    }
}
