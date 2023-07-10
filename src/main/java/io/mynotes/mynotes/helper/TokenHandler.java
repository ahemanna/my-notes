package io.mynotes.mynotes.helper;

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
    Utils utils;

    @Cacheable(cacheNames = "tokenCache", key = "'token'", unless = "#token != null")
    public Token getAccessToken() {
        Token token = null;

        String clientId = utils.getClientId();
        String clientSecret = utils.getClientSecret();
        String audience = utils.getAudience();
        String apiHost = utils.getApiHost();

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

            ObjectMapper mapper = new ObjectMapper();
            if(response.body() != null) {
                token = mapper.readValue(response.body().string(), Token.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return token;
    }
}
