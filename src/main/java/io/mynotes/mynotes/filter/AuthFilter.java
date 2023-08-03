package io.mynotes.mynotes.filter;

import io.mynotes.mynotes.helper.PropertiesHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Configuration
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    PropertiesHandler properties;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Enforce auth filter");

        String authHeader = request.getHeader("Authorization");
        String token;

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);

            String JWKS = properties.getApiHost() + "/.well-known/jwks.json";
            // https://github.com/felx/jose4j-wiki/blob/master/JWT%20Examples.md
            HttpsJwks httpsJwks = new HttpsJwks(JWKS);
            HttpsJwksVerificationKeyResolver httpsJwksVerificationKeyResolver =
                    new HttpsJwksVerificationKeyResolver(httpsJwks);

            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setExpectedAudience(properties.getApiAudience())
                    .setVerificationKeyResolver(httpsJwksVerificationKeyResolver)
                    .build();

            JwtClaims jwtClaims;
            try {
                jwtClaims = jwtConsumer.processToClaims(token);
                Collection<? extends GrantedAuthority> authorities = jwtClaims
                        .getClaimValue("permissions") == null? AuthorityUtils.NO_AUTHORITIES:
                        AuthorityUtils.createAuthorityList(jwtClaims.getClaimValueAsString("permissions"));

                User principal = new User(jwtClaims
                        .getClaimValue("username", String.class), "", authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(principal, token, authorities);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            } catch (InvalidJwtException | MalformedClaimException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Unauthorized");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NotNull HttpServletRequest request) {
        // https://stackoverflow.com/a/52370780/4658283
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return pathMatcher.match("/v1/register", request.getServletPath())
                || pathMatcher.match("/v1/authenticate", request.getServletPath());
    }
}
