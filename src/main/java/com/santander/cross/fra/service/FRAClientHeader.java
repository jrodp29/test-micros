package com.santander.cross.fra.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.santander.cross.fra.configuration.CacheHelper;
import com.santander.cross.fra.security.domain.FRAJwt;
import com.santander.cross.fra.security.domain.SecurityClient;
import io.micrometer.core.instrument.util.StringUtils;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@RegisterForReflection
public class FRAClientHeader implements ClientHeadersFactory {

    private final Logger log = LoggerFactory.getLogger(FRAClientHeader.class);

    public final String AUTHENTICATION_HEADER = "Authorization";
    public final String CACHE_JWT = "FRAJwt";
    public final String BEARER    = "Bearer  ";

    SecurityClient sc;
    CacheHelper ch;

    @Inject
    @RestClient
    FRATokenClientService ts;

    public FRAClientHeader(SecurityClient sc, CacheHelper ch) {
        this.sc  = sc;
        this.ch  = ch;
    }

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders, MultivaluedMap<String, String> clientOutgoingHeaders) {
        log.info("Performing security for FRA Service");
        try {
            String token = ch.getFRATokenCache().getIfPresent(CACHE_JWT);
            if (StringUtils.isEmpty(token)) {
                token = getJWT();
                if(null != token) {
                    ch.getFRATokenCache().put(CACHE_JWT, token);
                }
            } else {
                DecodedJWT djwt = JWT.decode(token);
                if(djwt.getExpiresAt().before(new Date())){
                    log.error("JWT is expired :'(");
                    token = getJWT();
                    if(null != token) {
                        ch.getFRATokenCache().put(CACHE_JWT, token);
                    }
                }
            }
            clientOutgoingHeaders.putSingle(AUTHENTICATION_HEADER, BEARER + token);
        } catch (Exception e) {
            log.error("Error occurred during get token", e);
        }
        return clientOutgoingHeaders;
    }

    private String resolveInputToken(String bearerToken) {
        if (null != bearerToken && bearerToken.length() > 0 && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String getJWT(){
        String token = null;
        try {
            CompletableFuture<FRAJwt> cts = ts.ctoken(sc.locale(), sc.apiconsumerid(), sc.apiconsumersecret()).toCompletableFuture();
            token = cts.get().getJwttoken();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } catch (ExecutionException e) {
            log.error(e.getMessage());
        }
        return token;
    }
}
