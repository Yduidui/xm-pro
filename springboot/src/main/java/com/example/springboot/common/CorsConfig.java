package com.example.springboot.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // Create a CORS configuration object
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("*");  // Allow all origins (you can restrict this to specific domains)
        corsConfig.addAllowedHeader("*");  // Allow all headers
        corsConfig.addAllowedMethod("*");  // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)

        // Register CORS configuration for all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        // Return a new CorsFilter using the configuration source
        return new CorsFilter(source);
    }
}
