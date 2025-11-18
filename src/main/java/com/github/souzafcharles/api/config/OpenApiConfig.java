package com.github.souzafcharles.api.config;

import com.github.souzafcharles.api.utils.Messages;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(Messages.OPENAPI_TITLE)
                        .version("1.0")
                        .description(Messages.OPENAPI_DESCRIPTION)
                );
    }
}
