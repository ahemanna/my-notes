package io.mynotes.mynotes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Auth0User {
    private String email;

    private String given_name;

    private String family_name;

    private String name;

    private String connection;

    private String password;
}
