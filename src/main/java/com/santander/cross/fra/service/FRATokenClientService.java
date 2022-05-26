package com.santander.cross.fra.service;

import com.santander.cross.fra.configuration.logger.ClientResponseLoggerFilter;
import com.santander.cross.fra.security.domain.FRAJwt;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.json.JsonObject;
import javax.ws.rs.*;
import java.util.concurrent.CompletionStage;

@Path("/AuthAuth_API/rest/encryption/v1")
@RegisterRestClient(configKey = "core-fra")
@RegisterProvider(ClientResponseLoggerFilter.class)
public interface FRATokenClientService {

    @GET
    @Path("/getApiToken")
    CompletionStage<FRAJwt> ctoken(@QueryParam("Locale") String locale,
                                   @QueryParam("ApiConsumerId") String apiconsumerid,
                                   @QueryParam("ApiConsumerSecret") String apiconsumersecret);

}
