package io.mynotes.mynotes.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mynotes.api.management.model.Token;
import io.mynotes.api.management.model.User;
import io.mynotes.mynotes.exception.BadRequestError;
import io.mynotes.mynotes.helper.TokenHandler;
import io.mynotes.mynotes.helper.Utils;
import io.mynotes.mynotes.model.Auth0Error;
import io.mynotes.mynotes.model.Auth0User;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService {

    @Autowired
    Utils utils;

    @Autowired
    TokenHandler tokenHandler;

    public User createUser(User user) {
        Token token = tokenHandler.getAccessToken();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");

        Auth0User auth0User = new Auth0User();
        User user1 = new User();

        auth0User.setEmail(user.getEmail());
        auth0User.setPassword(user.getPassword());
        auth0User.setGiven_name(user.getGivenName());
        auth0User.setFamily_name(user.getFamilyName());
        String CONNECTION = "Username-Password-Authentication";
        auth0User.setConnection(CONNECTION);

        ObjectMapper mapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = mapper.writeValueAsString(auth0User);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(requestBody, mediaType);
        Request request = new Request.Builder()
                .url(utils.getApiHost() + "/api/v2/users")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + token.getAccessToken())
                .build();
        try {
            Response response = client.newCall(request).execute();
            assert response.body() != null;

            if(!response.isSuccessful()) {
                Auth0Error auth0Error = mapper.readValue(response.body().string(), Auth0Error.class);
                if(response.code() == 400) {
                    throw new BadRequestError(auth0Error.getMessage());
                } else {
                    throw new RuntimeException(auth0Error.getMessage());
                }
            } else {
                Auth0User auth0User1 = mapper.readValue(response.body().string(), Auth0User.class);
                user1.setEmail(auth0User1.getEmail());
                user1.setFamilyName(auth0User1.getFamily_name());
                user1.setGivenName(auth0User1.getGiven_name());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return user1;
    }
}
