package com.santander.cross.fra.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.santander.cross.fra.configuration.CacheHelper;
import io.micrometer.core.instrument.util.StringUtils;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.Provider;
import java.security.Key;
import java.util.*;

@Provider
public class FRATokenProvider {

    private final Logger log = LoggerFactory.getLogger(FRATokenProvider.class);

    private Key key;

    private Algorithm algorithm;

    private final CacheHelper cacheHelper;

    public FRATokenProvider(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
        // final String secret = ConfigProvider.getConfig().getValue("quarkus.rest-client.scfhq-cross-audit-writer.base64-secret", String.class);
        final String secret = "NzdlZDk3ZWE2MzBjYTgxYjc3MWEwMjBmYzZlMzAzYzRmYWRjMDIzODNkNDc1NzU2ZWUxZDhiNzlmMWEzOTRiZTIxYWIyYTU5YTRhYTdjZTVhMGIwYjk1YjhiZDdkYWZmNDYzNzVhNzg5NWVmZWY0MDVkNjkxZjYyNzVjOWJlNTI=";
        if (null != secret && !secret.isEmpty()) {
            log.debug("Using a Base64-encoded JWT secret key");
            byte[] keyBytes = Base64.getDecoder().decode(secret);
            algorithm = Algorithm.HMAC512(keyBytes);
        } else {
            log.error("Error: No JWT key declared as scfhq-cross-audit-writer.base64-secret property");
        }
    }

    public String generateToken(final String sub) {
        String token = cacheHelper.getFRATokenCache().getIfPresent(sub);
        if (StringUtils.isEmpty(token)) {
            log.info("Generating new token for Audit Writer service");
            final Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.MINUTE, 30);
            final Map<String, String> claims = new HashMap<>();
            claims.put("auth", "ROLE_AUDIT");
            claims.put("sub", sub);
            token = JWT.create()
                    .withIssuer("https://private.gruposantander.com/issuer")
                    .withAudience("checkout-api")
                    .withClaim("auth", "ROLE_AUDIT")
                    .withSubject(sub)
                    .withExpiresAt(c.getTime())
                    .withJWTId(UUID.randomUUID().toString())
                    .sign(algorithm);
            cacheHelper.getFRATokenCache().put(sub, token);
        }
        return token;
    }

}
