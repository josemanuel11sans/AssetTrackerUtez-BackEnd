package com.integradora.AssetTrackerUtez.Cloudinary.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dic186agm",
                "api_key", "648767647435581",
                "api_secret", "RsaFgP9FyMv20YNBap2HHQy7COE"
        ));
    }
}