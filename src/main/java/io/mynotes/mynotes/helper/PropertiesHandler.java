package io.mynotes.mynotes.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
public class PropertiesHandler {
    @Autowired
    private Environment env;

    public PropertiesHandler(Environment env) {
        this.env = env;
    }

    public String getClientId() {
        return env.getProperty("auth0.client-id");
    }

    public String getClientSecret() {
        return env.getProperty("auth0.client-secret");
    }

    public String getAudience() {
        return env.getProperty("auth0.audience");
    }

    public String getApiHost() {
        return env.getProperty("auth0.api-host");
    }

    public long getCacheTtl() {
        return Long.parseLong(Objects.requireNonNull(env.getProperty("cache.ttl")));
    }
}
