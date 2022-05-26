package com.santander.cross.fra.security;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.santander.cross.fra.security.domain.SecurityKeys;

import com.auth0.jwt.JWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.Provider;
import java.util.*;
import java.util.stream.Collectors;


@Provider
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private final String AUTHORITIES_KEY = "auth";

    private final String INVALID_JWT_TOKEN = "Invalid JWT token.";

    Map<String, com.auth0.jwt.JWTVerifier> keyMap;

    public TokenProvider(SecurityKeys authenticationKeys) {
        log.info("Building TokenProvider");
        keyMap = new HashMap<>();
        log.info("Loading authorization keys in memory...");

        log.info("Auth keys are null? " + Boolean.toString(authenticationKeys == null));
        if (null == authenticationKeys || null == authenticationKeys.keys() || authenticationKeys.keys().isEmpty()) {
            log.error("Error: No Authorization key declared as security.authentication.keys[n]{kid,kty,n,e} properties");
        }

        authenticationKeys.keys().stream().forEach(auth -> {
            byte[] keyBytes = Base64.getDecoder().decode(auth.n());
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(keyBytes)).build(); //Reusable verifier instance

            keyMap.put(auth.kid(), verifier);
            log.info("Authorization key with kid " + auth.kid() + " loaded");
        });

       if (keyMap.isEmpty()) {
           log.error("No authentication key can be loaded");
       }
    }

    public String getSubject(String token, String kid) {
        return keyMap.get(kid).verify(token).getSubject();
    }

    public String getKid(String token) {
        return JWT.decode(token).getKeyId();
    }

    public boolean validateTokenAndRoles(final String kid, final String token, final String[] rolesRequired) {
        boolean result = false;
        try {
            final Map<String, Claim> claims = keyMap.get(kid).verify(token).getClaims();
            final List<String> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).asString().split(","))
                    .filter(auth -> !auth.trim().isEmpty())
                    .collect(Collectors.toList());
            result = !Collections.disjoint(Arrays.asList(rolesRequired), authorities);
        } catch (JWTVerificationException e) {
            log.warn(INVALID_JWT_TOKEN, e);
        } catch (Exception e) {
            log.error("Token validation error {}", e.getMessage());
        }
        return result;
    }
}
