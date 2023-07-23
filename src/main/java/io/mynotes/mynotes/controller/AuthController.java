package io.mynotes.mynotes.controller;

import io.mynotes.api.management.api.AuthApi;
import io.mynotes.api.management.model.GenerateTokenRequest;
import io.mynotes.api.management.model.Token;
import io.mynotes.api.management.model.User;
import io.mynotes.mynotes.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@RestController
public class AuthController implements AuthApi {
    @Autowired
    AuthService authService;
    @Override
    public Optional<NativeWebRequest> getRequest() {
        return AuthApi.super.getRequest();
    }

    @Override
    public ResponseEntity<User> createUser(User user) {
        User u = authService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(u);
    }

    @Override
    public ResponseEntity<Token> generateToken(GenerateTokenRequest generateTokenRequest) {
        Token t = authService.generateToken(generateTokenRequest);
        return ResponseEntity.status(HttpStatus.OK).body(t);
    }
}
