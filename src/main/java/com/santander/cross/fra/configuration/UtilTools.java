package com.santander.cross.fra.configuration;

import org.jboss.resteasy.specimpl.AbstractBuiltResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Provider
public class UtilTools {

    private final Logger log = LoggerFactory.getLogger(UtilTools.class);

    public String getBody(Object entity) {
        String body;
        try {
            InputStream is = (InputStream) entity;
            byte[] bytes = new byte[is.available()];
            is.read(bytes, 0, is.available());
            body = new String(bytes);
        } catch (IOException e) {
            body = new String("Error reading an error :)");
            log.error(e.getMessage());
        }
        return body;
    }
}
