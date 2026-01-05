package com.mycompany.app.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.app.model.dao.LocationRepository;
import com.mycompany.app.model.dao.ReservationRepository;
import com.mycompany.app.model.dao.ServiceRepository;
import com.mycompany.app.model.dao.UserRepository;
import com.mycompany.app.model.entities.*;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

@ApplicationScoped
public class SQLiteInitialize {
    private static final Logger LOGGER = Logger.getLogger(SQLiteInitialize.class.getName());

    @Inject
    ServiceRepository serviceRepository;

    @Inject
    ReservationRepository reservationRepository;

    @Inject
    LocationRepository locationRepository;

    @Inject
    UserRepository userRepository;

    void onStart(@Observes StartupEvent ev) {
        if(isDbEmpty()) {
            LOGGER.info("-------------Initializing database...---------------");
            initDb();
            LOGGER.info("-------------Database initialized. ---------------");
        }
        readTable(locationRepository);
        readTable(serviceRepository);
        readTable(userRepository);
        readTable(reservationRepository);
    }

    @Transactional
    public void initDb() {
        Service service = new Service("Golenie", new BigDecimal("30"));
        Service service2 = new Service("Strzyżenie", new BigDecimal("50"));
        User admin = new User("Admin", Role.OWNER, null);
        Location location = new Location("Tartak", 5, admin);
        Location location2 = new Location("Sieczkarnia",3, admin);
        User cashier = new User("Cashier1", Role.CASHIER, location);
        User employee = new User("Employee1", Role.EMPLOYEE, location);
        User client = new User("Client1", Role.CLIENT, location);
        User client2 = new User("Client2", Role.CLIENT, location);
        Reservation reservation = new Reservation(Date.valueOf("2025-12-21"), Time.valueOf("12:00:00"), location, service, employee, client);
        reservation.setBilled(true);
        Reservation reservation2 = new Reservation(Date.valueOf("2026-01-15"), Time.valueOf("11:30:00"), location, service2, employee, client);
        Reservation reservation3 = new Reservation(Date.valueOf("2026-02-10"), Time.valueOf("10:20:00"), location, service2, employee, client2);
        reservation3.setBilled(true);
        serviceRepository.persist(service);
        serviceRepository.persist(service2);
        userRepository.persist(admin);
        locationRepository.persist(location);
        locationRepository.persist(location2);
        userRepository.persist(cashier);
        userRepository.persist(employee);
        userRepository.persist(client);
        userRepository.persist(client2);
        reservationRepository.persist(reservation);
        reservationRepository.persist(reservation2);
        reservationRepository.persist(reservation3);
    }

    @Transactional
    public void readTable(PanacheRepository repository) {
        LOGGER.info("-------------Reading table...--------------");
        repository.findAll().stream().forEach(l -> writeAsString(l));
    }

    private boolean isDbEmpty() {
        return isUsersEmpty() && isServicesEmpty() && isReservationsEmpty() && isLocationsEmpty();
    }

    private boolean isUsersEmpty() {
        return 0 == userRepository.count();
    }

    private boolean isServicesEmpty() {
        return 0 == serviceRepository.count();
    }

    private boolean isReservationsEmpty() {
        return 0 == reservationRepository.count();
    }

    private boolean isLocationsEmpty() {
        return 0 == locationRepository.count();
    }

    static void writeAsString(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(o));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}