package com.mycompany.app.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;
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
        this(null, date, time, location, service, user, client);
    }
}
