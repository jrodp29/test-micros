package com.santander.cross.fra.configuration.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.IOException;

public class ClientResponseLoggerFilter implements ClientResponseFilter {

    private final Logger log = LoggerFactory.getLogger(ClientResponseLoggerFilter.class);

    @Override
    public void filter(ClientRequestContext clientRequestContext, ClientResponseContext clientResponseContext) throws IOException {
        log.info("Response client {} {}", clientResponseContext.getStatus(), clientResponseContext.getStatusInfo());
    }
}
