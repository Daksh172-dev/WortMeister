package com.wortmeister.ai;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.ai")
public record AiClientProperties(String baseUrl, String internalApiKey) {
}
