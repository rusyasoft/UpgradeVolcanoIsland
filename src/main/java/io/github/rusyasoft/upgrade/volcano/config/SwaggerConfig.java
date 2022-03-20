package io.github.rusyasoft.upgrade.volcano.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
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
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("io.github.rusyasoft.upgrade.volcano"))
                .build()
                .apiInfo(
                        new ApiInfo(
                                "Upgade Volcano Island",
                                "Booking Volcano Island",
                                "1.0",
                                "https://rusyasoft.github.io",
                                "Rustam",
                                "Rustam License",
                                "https://rusyasoft.github.io"
                        )
                );
    }
}
