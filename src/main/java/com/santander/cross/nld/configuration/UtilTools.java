package com.santander.cross.nld.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.json.XML;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;

@Provider
public class UtilTools {

    private final Logger log = LoggerFactory.getLogger(UtilTools.class);

    private static final int PRETTY_PRINT_INDENT_FACTOR = 4;

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

    public String convertXmlToJson(String xmlString) {
        JSONObject xmlJSONObj = XML.toJSONObject(xmlString, true);
        return xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
    }

    public String convertJsonToXml(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        return XML.toString(json);
    }

    public String readXmlFieldFromJson(String field, String payload) {
        JSONObject json = new JSONObject(payload);
        String xmlPayload = json.getString(field);
        return xmlPayload;
    }
}
