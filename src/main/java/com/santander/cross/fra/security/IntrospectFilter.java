package com.santander.cross.fra.security;

import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;

@Provider
@ApplicationScoped
public class IntrospectFilter implements ContainerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(IntrospectFilter.class);

    @Context
    ResourceInfo resourceInfo;

    @Inject
    TokenProvider tokenProvider;

    public final String AUTHENTICATION_HEADER = "Authorization";


    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        final Method method = resourceInfo.getResourceMethod();
        log.info("Authentication (JWT) is required");
        jwtAuthentication(containerRequestContext, method);
    }

    private void jwtAuthentication(ContainerRequestContext containerRequestContext, Method method) {
        try {
            final String[] roles = method.getAnnotation(RolesAllowed.class).value();
            final String jwt = resolveToken(containerRequestContext);
            final String kid = tokenProvider.getKid(jwt);
            log.info("JWT with kid " + kid + " received");

            if (StringUtils.isNotBlank(jwt) && this.tokenProvider.validateTokenAndRoles(kid, jwt, roles)) {
                log.info("JWT authentication done successfully");
            } else {
                log.error("Authentication invalid");
                abortRequest(Response.Status.UNAUTHORIZED, containerRequestContext);
                return;
            }
        } catch (Exception e){
            log.error("Authentication invalid -> {}", e.getMessage());
            abortRequest(Response.Status.UNAUTHORIZED, containerRequestContext);
            return;
        }
    }

    private String resolveToken(ContainerRequestContext containerRequestContext) {
        String bearerToken = containerRequestContext.getHeaders().getFirst(AUTHENTICATION_HEADER);
        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void abortRequest(final Response.Status unauthorized, final ContainerRequestContext containerRequestContext) {
        containerRequestContext.abortWith(Response.serverError().status(unauthorized).build());
    }
}
