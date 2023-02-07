package com.cts.dlp.configurations;

import javax.tools.DocumentationTool.DocumentationTask;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	
    @Bean
    public Docket api () {
    	
    	return new Docket(DocumentationType.SWAGGER_2)
    	 .select()
    	 .apis(RequestHandlerSelectors.basePackage("com.cts.dlp"))
    	 .paths(PathSelectors.any())
    	 .build()
    	  .apiInfo(apiInfo());
    }
    
    
    private ApiInfo apiInfo() {
    	 return new ApiInfoBuilder()
    	  .title("Authorization Service For Data Loader Portal")
    	  .description("The microservice is a part of Data Loader Portal Application and it deals with Authentication and Authorization")
    	  .license("1.0.0")
    	  .contact(new Contact("Cognizant Technology Solutions" , "https://cognizant.com" , "cognizant@gmail.com"))
    	  .build();
    }
}
