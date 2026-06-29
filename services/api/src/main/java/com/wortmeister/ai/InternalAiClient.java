package com.wortmeister.ai;

import java.time.Duration;
import java.util.Map;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class InternalAiClient {
    private final RestClient restClient;
    private final AiClientProperties properties;

    public InternalAiClient(RestClient.Builder builder, AiClientProperties properties) {
        this.properties = properties;
        this.restClient = builder.baseUrl(properties.baseUrl()).build();
    }

    public Map<?, ?> post(String path, Object request) {
        return restClient.post()
                .uri(path)
                .header("X-Internal-Api-Key", properties.internalApiKey())
                .body(request)
                .retrieve()
                .body(Map.class);
    }
}

@Configuration
class AiRestClientConfig {
    @Bean
    RestClientCustomizer aiRestClientCustomizer() {
        return builder -> {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(Duration.ofSeconds(3));
            requestFactory.setReadTimeout(Duration.ofSeconds(20));
            builder.requestFactory(requestFactory);
        };
    }
}
