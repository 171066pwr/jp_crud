package com.mycompany.app.controller;

import com.mycompany.app.model.dao.ReservationRepository;
import com.mycompany.app.model.dao.UserRepository;
import com.mycompany.app.model.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/client")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ClientController {
    @Inject
    ReservationRepository reservationRepository;

    @Inject
    UserRepository userRepository;

    @GET
    @Path("/reservations/{id}")
    public Response getReservations(@PathParam("id") Long userId) {
        User entity = userRepository.findById(userId);
        if(entity == null) {
            throw new ForbiddenException();
        }
        return Response.ok(reservationRepository.findActiveAnonymised(entity)).build();
    }
}
