package io.mynotes.mynotes.helper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mynotes.api.management.model.Token;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class TokenHandler {
    @Autowired
    PropertiesHandler properties;

    @Cacheable(cacheNames = "tokenCache", key = "'token'", unless = "#token != null")
    public Token getAccessToken() {
        Token token = null;

        String clientId = properties.getClientId();
        String clientSecret = properties.getClientSecret();
        String audience = properties.getAudience();
        String apiHost = properties.getApiHost();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create("grant_type=client_credentials&client_id=" + clientId +
                "&client_secret=" + clientSecret + "&audience=" + audience, mediaType);
        Request request = new Request.Builder()
                .url(apiHost + "/oauth/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.code() != 200) {
                System.out.println("StatusCode :: " + response.code());
                System.out.println("Response :: " + (response.body() != null ? response.body().string() : null));
                throw new RuntimeException("Error generating access token");
            }
            if (response.body() != null) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                token = mapper.readValue(response.body().string(), Token.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return token;
    }
}
