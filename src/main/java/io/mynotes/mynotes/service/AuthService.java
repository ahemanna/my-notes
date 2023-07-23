package io.mynotes.mynotes.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mynotes.api.management.model.GenerateTokenRequest;
import io.mynotes.api.management.model.Token;
import io.mynotes.api.management.model.User;
import io.mynotes.mynotes.exception.BadRequestError;
import io.mynotes.mynotes.exception.ConflictError;
import io.mynotes.mynotes.helper.Helper;
import io.mynotes.mynotes.helper.PropertiesHandler;
import io.mynotes.mynotes.helper.TokenHandler;
import io.mynotes.mynotes.model.Auth0Error;
import io.mynotes.mynotes.model.Auth0User;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService {

    @Autowired
    PropertiesHandler properties;

    @Autowired
    TokenHandler tokenHandler;

    public User createUser(User user) {
        Token token = tokenHandler.getAccessToken();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");

        Auth0User auth0User = Helper.mapUser2Auth0User(user);
        auth0User.setName(user.getFirstName() + " " + user.getLastName());
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
                .url(properties.getApiHost() + "/api/v2/users")
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
                } else if (response.code() == 409) {
                    throw new ConflictError(auth0Error.getMessage());
                } else {
                    throw new RuntimeException(auth0Error.getMessage());
                }
            } else {
                Auth0User newUser = mapper.readValue(response.body().string(), Auth0User.class);
                return Helper.mapAuth0User2User(newUser);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Token generateToken(GenerateTokenRequest generateTokenRequest) {
        System.out.println(generateTokenRequest.toString());

        return null;
    }
}
