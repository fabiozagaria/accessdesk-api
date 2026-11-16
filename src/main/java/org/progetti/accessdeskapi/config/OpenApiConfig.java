package org.progetti.accessdeskapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;

@Configuration
public class OpenApiConfig {

    private static final String BEARER = "bearer-jwt";

    /** Metadati e schema di sicurezza condivisi da tutti i gruppi. */
    @Bean
    public OpenAPI openApi(@Value("${spring.application.name}") String name,
                           @Value("${app.version:dev}") String version) {
        return new OpenAPI()
                .info(new Info().title(name).version(version))
                .components(new Components().addSecuritySchemes(BEARER,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    /** Endpoint pubblici: nessun lucchetto. */
    @Bean
    public GroupedOpenApi authGroup() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/auth/**")
                .build();
    }

    /**
     * Per aggiungere un'area (es. /api/reports) basta un altro bean
     * GroupedOpenApi con il suo pathsToMatch: niente altro da toccare.
     */
    @Bean
    public GroupedOpenApi requestsGroup() {
        return GroupedOpenApi.builder()
                .group("requests")
                .pathsToMatch("/api/requests/**")
                .addOpenApiCustomizer(api ->
                        api.addSecurityItem(new SecurityRequirement().addList(BEARER)))
                .build();
    }
}
