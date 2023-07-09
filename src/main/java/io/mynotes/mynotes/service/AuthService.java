package io.mynotes.mynotes.service;

import io.mynotes.api.management.model.User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public User createUser(User user) {
        user.setPassword(null);
        return user;
    }
}
