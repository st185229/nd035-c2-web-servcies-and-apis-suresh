package com.udacity.vehicles.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket vehiclesApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.udacity.vehicles.api"))
                .paths(regex("/cars.*"))
                .build()
                .apiInfo(apiInfo());

    }
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Udacity Vehicles API",
                "Vehicle CRUD APIS",
                "0.2.alpha",
                "https://www.udacity.com/legal/en-eu/terms-of-use",
                new Contact("Udacity", "www.udacity.com", "dummy-mail@udacity.com"),
                "MIT Licence", "https://opensource.org/licenses/MIT", Collections.emptyList());


    }
}
