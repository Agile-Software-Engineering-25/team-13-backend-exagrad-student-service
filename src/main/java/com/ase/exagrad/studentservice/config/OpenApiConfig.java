package com.ase.exagrad.studentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("ExaGrad Student Service API")
            .version("1.0.0")
            .description("API for managing exam documents and PUB documents for students")
            .contact(new Contact()
                .name("ASE Team 13")
                .email("simon.dietrich@stud-provadis-hochschule.de")));
  }
}
