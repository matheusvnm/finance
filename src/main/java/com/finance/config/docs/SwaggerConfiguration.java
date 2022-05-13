package com.finance.config.docs;

import com.finance.domain.Usuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ParameterType;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

@Configuration
@Profile("prod")
public class SwaggerConfiguration {

    @Bean
    public Docket getApiForumDocumentation() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.finance"))
                .paths(PathSelectors.ant("/**"))
                .build()
                .ignoredParameterTypes(Usuario.class)
                .globalRequestParameters(List.of(new RequestParameterBuilder().name("Authorization")
                        .in(ParameterType.HEADER)
                        .description("JWT Auth Token")
                        .required(false)
                        .accepts(List.of(MediaType.APPLICATION_JSON))
                        .build()));
    }
}
