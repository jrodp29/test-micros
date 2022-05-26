package com.santander.cross.fra.web.rest;

import com.santander.cross.fra.configuration.UtilTools;
import com.santander.cross.fra.service.FRAClientService;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

@Path("/Simulation_API/rest/quoting/v1")
public class FRAResource {

    private final Logger log = LoggerFactory.getLogger(FRAResource.class);

    @Inject
    @RestClient
    FRAClientService cs;

    @Inject
    UtilTools ut;

    @POST
    @Path("/simulation/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "ROLE_MGATEWAY" })
    public CompletableFuture<Response> simulation(final String contentMessage,
                                                  @HeaderParam("Authorization") final String inputAuthorization,
                                                  @QueryParam("pointOfSale")    final String pointOfSale) {
        log.info("Request received in simulation method");
        return cs.cmultisimulation(contentMessage, pointOfSale, inputAuthorization).whenComplete((rsp, exc) ->{
            if(null != exc) {
                if(exc.getCause() instanceof WebApplicationException){
                    WebApplicationException wexc = (WebApplicationException) exc.getCause();
                    log.error("WebApplicationException #> " + wexc.getResponse().getStatus());
                } else {
                    log.error(exc.getMessage());
                }
            }
        }).thenApply(rsp -> {
            return Response.status(200).entity(rsp).build();
        }).exceptionally(exc ->{
            WebApplicationException wexc = (WebApplicationException) exc.getCause();
            return Response.status(wexc.getResponse().getStatus()).entity(ut.getBody(wexc.getResponse().getEntity())).build();
        }).toCompletableFuture();
    }

    @GET
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "ROLE_MGATEWAY" })
    public CompletableFuture<Response> products(@HeaderParam("Authorization") final String inputAuthorization,
                                                @QueryParam("pointOfSale")    final String pointOfSale) {
        log.info("Request received in products method");
        return cs.cproducts(pointOfSale, inputAuthorization).whenComplete((rsp, exc) ->{
            if(null != exc) {
                if(exc.getCause() instanceof WebApplicationException){
                    WebApplicationException wexc = (WebApplicationException) exc.getCause();
                    log.error("WebApplicationException #> " + wexc.getResponse().getStatus());
                } else {
                    log.error(exc.getMessage());
                }
            }
        }).thenApply(rsp -> {
            return Response.status(200).entity(rsp).build();
        }).exceptionally(exc ->{
            WebApplicationException wexc = (WebApplicationException) exc.getCause();
            return Response.status(wexc.getResponse().getStatus()).entity(ut.getBody(wexc.getResponse().getEntity())).build();
        }).toCompletableFuture();
    }
}
