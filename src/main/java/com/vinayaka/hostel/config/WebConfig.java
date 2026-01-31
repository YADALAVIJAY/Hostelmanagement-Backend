package com.vinayaka.hostel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        
        // ⚠️ REPLACE THIS with your specific deployed Frontend URL
        // Example: "https://my-hostel-app.vercel.app"
        // NOTE: Do not add a trailing slash '/' at the end of the URL
        String myFrontendUrl = "https://YOUR-FRONTEND-APP.vercel.app";
        registry.addMapping("/**")
                .allowedOrigins(myFrontendUrl) 
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}