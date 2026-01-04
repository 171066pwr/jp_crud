package com.mycompany.app.controller;

import com.mycompany.app.model.dao.ReservationRepository;
import com.mycompany.app.model.dao.ServiceRepository;
import com.mycompany.app.model.entities.Service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.Date;

@Path("/owner")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class OwnerController {
    @Inject
    ReservationRepository reservationRepository;

    @Inject
    ServiceRepository serviceRepository;

    @GET
    @Path("/services")
    public Response getServices() {
        return Response.ok(serviceRepository.findAll().list()).build();
    }

    @POST
    @Transactional
    public Response createService(Service service) {
        serviceRepository.persist(service);
        return Response.noContent().build();
    }

    @PUT
    @Path("/services/{id}")
    @Transactional
    public Response updateService(@PathParam("id") Long id, Service service) {
        Service entity = serviceRepository.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        entity.copyFrom(service);
        serviceRepository.persist(entity);
        return Response.ok(entity).build();
    }

    @GET
    @Path("/reservations")
    @Produces("application/json")
    public Response getReservations(@DefaultValue("1970-01-01") @QueryParam("from") Date from,
                                             @DefaultValue("2100-01-01") @QueryParam ("to") Date to) {
        return Response.ok(reservationRepository.findByDate(from, to)).build();
    }

    @GET
    @Path("/reservations/income")
    public Response getReservationsIncome(@DefaultValue("1970-01-01") @QueryParam("from") Date from,
                                          @DefaultValue("2100-01-01") @QueryParam("to") Date to,
                                          @DefaultValue("True") @QueryParam("billed") Boolean realized) {
        return Response.ok(reservationRepository.getIncomeForPeriod(from, to, realized)).build();
    }
}
