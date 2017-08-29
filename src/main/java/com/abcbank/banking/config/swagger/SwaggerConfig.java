package com.abcbank.banking.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         28/08/17
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.abcbank.banking"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        VendorExtension vendorExtension = new StringVendorExtension("extension", null);
        return new ApiInfo("ABC Bank",
                "ABC Bank API docs",
                "1.0", "", new Contact("Shyam Anand", "shyam-anand.com", "shyamwdr@gmail.com"), "", "", Collections.singleton(vendorExtension));
    }
}
