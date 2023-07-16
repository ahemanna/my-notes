package io.mynotes.mynotes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Auth0Error {
    private int statusCode;

    private String error;

    private String message;

    private String errorCode;
}
