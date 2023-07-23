package io.mynotes.mynotes.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mynotes.api.management.model.GenerateTokenRequest;
import io.mynotes.api.management.model.Token;
import io.mynotes.api.management.model.User;
import io.mynotes.mynotes.exception.BadRequestError;
import io.mynotes.mynotes.exception.ConflictError;
import io.mynotes.mynotes.exception.ForbiddenError;
import io.mynotes.mynotes.helper.Helper;
import io.mynotes.mynotes.helper.PropertiesHandler;
import io.mynotes.mynotes.helper.TokenHandler;
import io.mynotes.mynotes.model.Auth0ApiError;
import io.mynotes.mynotes.model.Auth0OauthError;
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
        Token token = tokenHandler.generateAccessToken();

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

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
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
                Auth0ApiError auth0ApiError = mapper.readValue(response.body().string(), Auth0ApiError.class);
                if(response.code() == 400) {
                    throw new BadRequestError(auth0ApiError.getMessage());
                } else if (response.code() == 409) {
                    throw new ConflictError(auth0ApiError.getMessage());
                } else {
                    throw new RuntimeException(auth0ApiError.getMessage());
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

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(
                "grant_type=password" +
                "&username=" + generateTokenRequest.getEmail() +
                "&password=" + generateTokenRequest.getPassword() +
                "&client_id=" + properties.getClientId() +
                "&client_secret=" + properties.getClientSecret() +
                "&audience=" + properties.getApiAudience(), mediaType);
        Request request = new Request.Builder()
                .url(properties.getApiHost() + "/oauth/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        ObjectMapper mapper = new ObjectMapper();

        try {
            Response response = client.newCall(request).execute();
            assert response.body() != null;

            if(!response.isSuccessful()) {
                Auth0OauthError auth0OauthError = mapper.readValue(response.body().string(), Auth0OauthError.class);
                if(response.code() == 403) {
                    throw new ForbiddenError(auth0OauthError.getError_description());
                } else {
                    throw new RuntimeException("Something went wrong!");
                }
            } else {
                return mapper.readValue(response.body().string(), Token.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
