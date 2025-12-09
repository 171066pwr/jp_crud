package com.mycompany.app.model.entities;

import java.sql.Time;
import java.sql.Date;

public class Reservation {
    private Long id;
    private int location_id;
    private Date date;
    private Time time;
    private int service_id;
    private int employee_id;
    private int client_id;
}
