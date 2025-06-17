package com.rafael.spring.security.postgresql.SpringBootSecurityPostgresqlApplication.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Expone el contenido de la carpeta local /uploads como ruta accesible desde el navegador
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
