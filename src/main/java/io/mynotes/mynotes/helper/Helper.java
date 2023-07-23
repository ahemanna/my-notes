package io.mynotes.mynotes.helper;

import io.mynotes.api.management.model.User;
import io.mynotes.mynotes.model.Auth0User;
import org.modelmapper.ModelMapper;

public class Helper {

    // Reference: http://modelmapper.org/getting-started/#handling-mismatches
    public static User mapAuth0User2User(Auth0User auth0User) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Auth0User.class, User.class)
                .addMappings(m -> {
                    m.map(Auth0User::getGiven_name, User::setFirstName);
                    m.map(Auth0User::getFamily_name, User::setLastName);
                    m.map(Auth0User::getEmail, User::setEmail);
                });


        return modelMapper.map(auth0User, User.class);
    }

    public static  Auth0User mapUser2Auth0User(User user) {
        ModelMapper modelMapper = new ModelMapper();
        // To ignore Auth0User.setName during mapping
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.typeMap(User.class, Auth0User.class)
                .addMappings(m -> {
                   m.map(User::getFirstName, Auth0User::setGiven_name);
                   m.map(User::getLastName, Auth0User::setFamily_name);
                   m.map(User::getEmail, Auth0User::setEmail);
                   m.map(User::getPassword, Auth0User::setPassword);
                });

        return modelMapper.map(user, Auth0User.class);
    }
}
