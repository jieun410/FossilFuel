package edu.example.springbootblog.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info=@Info (
                title = "REST API",
                version = "ver 0.1",
                description = "RESTful API Documentation"
        ),
        servers = { @Server(
                description = "Prod ENV",
                url = "http://localhost:8060/"
        ),
                @Server(
                        description = "Staging ENV",
                        url = "http://localhost:8060/staging"
                )
        }
)
public class DocsApiConfig {
}




