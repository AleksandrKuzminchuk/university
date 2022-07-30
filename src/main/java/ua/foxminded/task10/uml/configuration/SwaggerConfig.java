package ua.foxminded.task10.uml.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ua.foxminded.task10.uml.controller.rest"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return (new ApiInfoBuilder())
                .title("Многострадальный проект Swagger")
                .description("Проект Spring Boot PI Swagger")
                .termsOfServiceUrl("http://localhost:8080/")
                .contact(new Contact(
                        "Александр Кузьминчук",
                        "-",
                        "lgoptimusg252@gmail.com"))
                .version("1.0")
                .build();
    }
}
