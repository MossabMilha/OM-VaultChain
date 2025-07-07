package com.omvaultchain.storage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pinata")
@Data
public class StorageConfig {
    private String baseUrl;
    private String pinFileUrl;
    private String apiKey;
    private String secretApiKey;
}
