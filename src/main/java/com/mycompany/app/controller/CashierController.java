package com.mycompany.app.controller;

import com.mycompany.app.model.dao.ReservationRepository;
import com.mycompany.app.model.dao.UserRepository;
import com.mycompany.app.model.entities.Reservation;
import com.mycompany.app.model.entities.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/cashier")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CashierController {
    @Inject
    ReservationRepository reservationRepository;

    @Inject
    UserRepository userRepository;

    @GET
    @Path("/reservations/{id}")
    public List<Reservation> getReservations(@PathParam("id") Long userId) {
        User entity = findUserById(userId);
        return reservationRepository.findByLocation(entity.getLocation());
    }

    @PUT
    @Path("/reservations/{id}")
    @Transactional
    public Response updateReservation(@PathParam("id") Long id, @QueryParam("userId") Long userId, @QueryParam("billed  ") Boolean billed) {
        User user = findUserById(userId);
        Reservation entity = reservationRepository.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        if(!entity.getLocation().equals(user.getLocation())) {
            throw new ForbiddenException();
        }
        entity.setBilled(billed);
        reservationRepository.persist(entity);
        return Response.ok(entity).build();
    }

    private User findUserById(Long userId) {
        User entity = userRepository.findById(userId);
        if(entity == null) {
            throw new NotFoundException();
        }
        return entity;
    }
}
