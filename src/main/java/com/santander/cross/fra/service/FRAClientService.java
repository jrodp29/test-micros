package com.santander.cross.fra.service;

import com.santander.cross.fra.configuration.logger.ClientResponseLoggerFilter;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import java.util.concurrent.CompletionStage;

@Path("/Simulation_API/rest/quoting/v1")
@RegisterRestClient(configKey = "core-fra")
@RegisterClientHeaders(FRAClientHeader.class)
@RegisterProvider(ClientResponseLoggerFilter.class)
public interface FRAClientService {

    @GET
    @Path("/products")
    CompletionStage<String> cproducts(@QueryParam("pointOfSale") String pointOfSale,
                                      @HeaderParam("Authorization") String inputAuthorization);

    @POST
    @Path("/simulation/create")
    CompletionStage<String> cmultisimulation(String form,
                                             @QueryParam("pointOfSale") String pointOfSale,
                                             @HeaderParam("Authorization") String inputAuthorization);

}
