package com.nitconfbackend.nitconf.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        title = "NitConf API",
        version = "v1",
        description = "Open API Document for NITCONF"
    ),
    servers = {
        @Server(
            url = "http://localhost:8082",
            description = "Local server"
        )
    }
)
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT Token for Authentication",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER

)
public class OpenApiConfig {
    
}
