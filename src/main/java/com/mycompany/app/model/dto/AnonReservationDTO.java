package com.mycompany.app.model.dto;

import com.mycompany.app.model.entities.Location;
import com.mycompany.app.model.entities.Reservation;
import com.mycompany.app.model.entities.User;
import lombok.Getter;

import java.sql.Date;
import java.sql.Time;

@Getter
public class AnonReservationDTO {
    private final Long id;
    private final Date date;
    private final Time time;
    private final Location location;
    private final Boolean isReservationOwner;

    public AnonReservationDTO(Reservation reservation, User user) {
        this.id = reservation.getId();
        this.date = reservation.getDate();
        this.time = reservation.getTime();
        this.location = reservation.getLocation();
        isReservationOwner = reservation.getClient().equals(user);
    }
}
