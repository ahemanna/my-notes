package io.mynotes.mynotes.service;

import io.mynotes.api.management.model.Token;
import io.mynotes.api.management.model.User;
import io.mynotes.mynotes.helper.TokenHandler;
import io.mynotes.mynotes.helper.Utils;
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
//        "email: john.doe@gmail.com,
//        blocked: false,
//        email_verified: false,
//        given_name: John
//        family_name: Doe,
//        name: John Doe,
//        nickname: Johnny,
//        connection: Username-Password-Authentication,
//        password: xxx,
//        verify_email: false
        RequestBody body = RequestBody.create("{\n    \"email\": \"john.doe@gmail.com\",\n    \"blocked\": false,\n    \"email_verified\": false,\n    \"given_name\": \"John\",\n    \"family_name\": \"Doe\",\n    \"name\": \"John Doe\",\n    \"nickname\": \"Johnny\",\n    \"picture\": \"https://secure.gravatar.com/avatar/15626c5e0c749cb912f9d1ad48dba440?s=480&r=pg&d=https%3A%2F%2Fssl.gstatic.com%2Fs2%2Fprofiles%2Fimages%2Fsilhouette80.png\",\n    \"connection\": \"Username-Password-Authentication\",\n    \"password\": \"TestPassword123!\",\n    \"verify_email\": false\n}", mediaType);
        Request request = new Request.Builder()
                .url(utils.getApiHost() + "/api/v2/users")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", token.getAccessToken())
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return user;
    }
}
