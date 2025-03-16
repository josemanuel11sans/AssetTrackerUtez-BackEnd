package com.integradora.AssetTrackerUtez.Cloudinary.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    // Se obtienen los valores de las variables de entorno
      @Value("${cloudinary.cloud_name}")
        private String cloudName;
      @Value("${cloudinary.api_key}")
        private String apiKey;
      @Value("${cloudinary.api_secret}")
        private String apiSecret;
        // Se crea un bean de Cloudinary con los valores de las variables de entorno
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }
}