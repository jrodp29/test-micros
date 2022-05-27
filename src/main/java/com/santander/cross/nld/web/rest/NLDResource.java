package com.santander.cross.nld.web.rest;

import com.santander.cross.nld.configuration.UtilTools;
import com.santander.cross.nld.service.NLDClientService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;

@Path("/Calculator")
public class NLDResource {

    private final Logger log = LoggerFactory.getLogger(NLDResource.class);

    @Inject
    @RestClient
    NLDClientService cs;

    @Inject
    UtilTools ut;

    @GET
    @Path("/{partner}/fullResponse")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "ROLE_MGATEWAY" })
    public CompletableFuture<Response> multisimulation(@PathParam("partner")         final String partner,
                                                       @HeaderParam("Authorization") final String inputAuthorization,
                                                       @QueryParam("loanAmount")     final String loanAmount,
                                                       @QueryParam("term")           final String term,
                                                       @QueryParam("balloon")        final String balloon,
                                                       @QueryParam("downpayment")    final String downpayment) {
        log.info("Request received in products method");
        return cs.cmultisimulation(partner, loanAmount, term, balloon, downpayment).whenComplete((rsp, exc) ->{
            if(null != exc) {
                if(exc.getCause() instanceof WebApplicationException){
                    WebApplicationException wexc = (WebApplicationException) exc.getCause();
                    log.error("WebApplicationException #> " + wexc.getResponse().getStatus());
                } else {
                    log.error(exc.getMessage());
                }
            }
        }).thenApply(rsp -> {
            rsp = ut.convertXmlToJson(rsp);
            return Response.status(200).entity(rsp).build();
        }).exceptionally(exc ->{
            WebApplicationException wexc = (WebApplicationException) exc.getCause();
            return Response.status(wexc.getResponse().getStatus()).entity(ut.getBody(wexc.getResponse().getEntity())).build();
        }).toCompletableFuture();
    }
}
