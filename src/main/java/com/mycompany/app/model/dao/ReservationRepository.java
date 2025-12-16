package com.mycompany.app.model.dao;

import com.mycompany.app.model.entities.Reservation;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReservationRepository implements PanacheRepository<Reservation> {
}
