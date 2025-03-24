package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // ✅ Allowed Origins (Adjust for Hosted Frontend)
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173","https://salessavvy-frontend.vercel.app"));

        // ✅ Allowed HTTP Methods
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // ✅ Allowed Headers
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With",
                "Origin"
        ));

        // ✅ Exposed Headers (if needed)
        config.setExposedHeaders(Arrays.asList("Authorization","Set-Cookie"));

        // ✅ Allow Cookies & Credentials
        config.setAllowCredentials(true);

        // ✅ Max Age for Preflight Caching
        config.setMaxAge(3600L);

        // ✅ Apply Configuration to All Endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        
        System.out.println(" CORS Config Initialized");
        System.out.println("✅ CORS Filter Applied with Origin: " + config.getAllowedOrigins());

        return new CorsFilter(source);
    }
}
