package com.ja.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // ✅ ALLOW BOTH LOCAL + PROD FRONTEND
        config.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "https://uptrixhub.online",
                "https://uptrix-hub.vercel.app"

        ));

        // ✅ REQUIRED METHODS
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        // ✅ ALLOW ALL HEADERS (Authorization, Content-Type, etc.)
        config.setAllowedHeaders(List.of("*"));

        // ✅ REQUIRED FOR JWT + OAUTH
        config.setAllowCredentials(true);

        // ✅ OPTIONAL BUT GOOD
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
