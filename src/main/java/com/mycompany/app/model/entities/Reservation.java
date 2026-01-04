package com.mycompany.app.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation implements Copiable<Reservation> {
    @Id
    @GeneratedValue
    private Long id;
    private Boolean billed;
    private Date date;
    private Time time;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Location location;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Service service;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private User employee;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private User client;

    public Reservation(Date date, Time time, Location location, Service service, User user, User client) {
        this(null, false, date, time, location, service, user, client);
    }

    @Override
    public void copyFrom(Reservation reservation) {
        this.billed = reservation.billed;
        this.date = reservation.date;
        this.time = reservation.time;
        this.location = reservation.location;
        this.service = reservation.service;
        this.employee = reservation.employee;
        this.client = reservation.client;
    }
}
