package com.br.meu_ape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApiPrefixConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // Adiciona o prefixo "/api/v1" a qualquer classe que tenha a anotação @RestController
        configurer.addPathPrefix(
                "/api/v1",
                HandlerTypePredicate.forAnnotation(RestController.class)
        );
    }
}
