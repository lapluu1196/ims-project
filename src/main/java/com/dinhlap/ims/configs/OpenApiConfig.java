package com.dinhlap.ims.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("IMS API Documentation")
                .version("1.0")
                .description("""
                        ## IMS API Documentation
                        
                        **For testing, please use the following credentials:**
                        
                        Admin account
                        - **Username:** `admin1`  
                        - **Password:** `password123`
                        
                        Recruiter account
                        - **Username:** `recruiter1`  
                        - **Password:** `password123`
                        
                        Interviewer account
                        - **Username:** `interviewer1`  
                        - **Password:** `password123`
                        
                        Manager account
                        - **Username:** `manager1`  
                        - **Password:** `password123`
                        """
                )
                .contact(new Contact()
                        .name("Dinh Lap")
                        .email("lapld.qtkd@gmail.com"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer"))
                .components(new Components()
                        .addSecuritySchemes("Bearer", new SecurityScheme()
                                .name("Bearer")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
