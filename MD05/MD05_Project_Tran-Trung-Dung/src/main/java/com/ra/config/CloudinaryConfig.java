package com.ra.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "daculcsoq",
                "api_key", "327495715166338",
                "api_secret", "mxjKW6wwHOxppqcCKQEqbXyE33E",
                "secure", true
        ));
    }
}
