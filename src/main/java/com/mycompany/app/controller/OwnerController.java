package com.mycompany.app.controller;

import com.mycompany.app.model.dao.ReservationRepository;
import com.mycompany.app.model.dao.ServiceRepository;
import com.mycompany.app.model.entities.Service;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.net.URI;
import java.sql.Date;

@Path("/owner")
@Produces(MediaType.TEXT_HTML)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class OwnerController {
    @Inject
    ReservationRepository reservationRepository;

    @Inject
    ServiceRepository serviceRepository;

    @Location("services.html")
    Template servicesTemplate;

    @GET
    @Path("/services")
    public TemplateInstance getServices() {
        return servicesTemplate.data("services", serviceRepository.findAll().list());
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    @Path("/services/create")
    public Response createService(@FormParam("name") String name, @FormParam("price") String price) {
        var service = new Service(name, new BigDecimal(price));
        serviceRepository.persist(service);
        return Response.seeOther(URI.create("/owner/services"))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    @Path("/services/update")
    public Response updateService(@FormParam("id") Long id, @FormParam("name") String name, @FormParam("price") BigDecimal price) {
        Service entity = serviceRepository.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        entity.setName(name);
        entity.setPrice(price);
        serviceRepository.persist(entity);
        return Response.seeOther(URI.create("/owner/services"))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    @Path("/services/{id}/delete")
    public Response deleteService(@PathParam("id") Long id) {
        Service entity = serviceRepository.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        serviceRepository.delete(entity);
        return Response.seeOther(URI.create("/owner/services"))
                .build();
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
