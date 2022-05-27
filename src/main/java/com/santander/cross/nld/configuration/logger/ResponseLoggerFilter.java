package com.santander.cross.nld.configuration.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class ResponseLoggerFilter implements ContainerResponseFilter {

    private final Logger log = LoggerFactory.getLogger(ResponseLoggerFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        log.info("Response [{}] returned with status {} ", containerRequestContext.getProperty("X-Request-ID"), containerResponseContext.getStatus(), containerResponseContext.getStatusInfo());
    }
}
