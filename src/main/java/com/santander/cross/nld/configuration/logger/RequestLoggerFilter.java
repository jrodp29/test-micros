package com.santander.cross.nld.configuration.logger;

import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.util.UUID;

@Provider
@PreMatching
public class RequestLoggerFilter implements ContainerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(RequestLoggerFilter.class);

    @Context
    UriInfo info;

    @Context
    HttpServerRequest request;

    @Override
    public void filter(ContainerRequestContext context) {

        final String method = context.getMethod();
        final String path = info.getPath();
        final String address = request.remoteAddress().toString();

        final String requestId = context.getHeaders().containsKey("X-Request-ID")
                ? context.getHeaderString("X-Request-ID")
                : UUID.randomUUID().toString();
        context.setProperty("X-Request-ID", requestId);

        log.info("Request [{}] received: {} {} from IP {}", requestId, method, path, address);
    }
}
