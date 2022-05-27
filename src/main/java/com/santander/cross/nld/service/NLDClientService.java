package com.santander.cross.nld.service;

import com.santander.cross.nld.configuration.logger.ClientResponseLoggerFilter;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import java.util.concurrent.CompletionStage;

@Path("/Calculator")
@RegisterRestClient(configKey = "core-nld")
@RegisterProvider(ClientResponseLoggerFilter.class)
public interface NLDClientService {

    @GET
    @Path("/{partner}/fullResponse")
    CompletionStage<String> cmultisimulation(@PathParam("partner")      String partner,
                                             @QueryParam("loanAmount")  String loanAmount,
                                             @QueryParam("term")        String term,
                                             @QueryParam("balloon")     String balloon,
                                             @QueryParam("downpayment") String downpayment);

}
