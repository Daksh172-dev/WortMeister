package com.wortmeister.auth;

import com.wortmeister.common.error.ApiException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Component
public class GoogleTokenInfoVerifier implements GoogleTokenVerifier {
    private final GoogleProperties properties;
    private final RestClient restClient;

    public GoogleTokenInfoVerifier(GoogleProperties properties, RestClient.Builder builder) {
        this.properties = properties;
        this.restClient = builder.baseUrl("https://oauth2.googleapis.com").build();
    }

    @Override
    public GoogleIdentity verify(String idToken) {
        if (!StringUtils.hasText(properties.clientId())) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "GOOGLE_LOGIN_NOT_CONFIGURED",
                    "Google login is not configured.");
        }
        Map<String, Object> response = restClient.get()
                .uri(uri -> uri.path("/tokeninfo").queryParam("id_token", idToken).build())
                .retrieve()
                .body(Map.class);
        if (response == null || !properties.clientId().equals(response.get("aud"))) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "INVALID_GOOGLE_TOKEN", "Invalid Google token.");
        }
        String email = String.valueOf(response.get("email"));
        boolean verified = Boolean.parseBoolean(String.valueOf(response.get("email_verified")));
        if (!verified) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "GOOGLE_EMAIL_UNVERIFIED", "Google email is not verified.");
        }
        return new GoogleIdentity(String.valueOf(response.get("sub")), email, true,
                String.valueOf(response.getOrDefault("name", "Google Learner")),
                String.valueOf(response.getOrDefault("picture", "")));
    }
}
