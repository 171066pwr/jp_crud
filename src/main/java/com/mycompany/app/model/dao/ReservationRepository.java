package com.mycompany.app.model.dao;

import com.mycompany.app.model.dto.AnonReservationDTO;
import com.mycompany.app.model.entities.Location;
import com.mycompany.app.model.entities.Reservation;
import com.mycompany.app.model.entities.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class ReservationRepository implements PanacheRepository<Reservation> {
    public BigDecimal getIncomeForPeriod(Date start, Date end, Boolean realized) {
        return stream("date BETWEEN ?1 AND ?2 AND billed = ?3", start, end, realized)
                .map(r -> r.getService().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Reservation> findByDate(Date start, Date end) {
        return list("date BETWEEN ?1 AND ?2", start, end);
    }

    public List<Reservation> findByUser(User user) {
        return list("employee = ?1 OR client = ?1 ORDER BY date DESC", user);
    }

    public List<Reservation> findByLocation(Location location) {
        return list("location = ?1 ORDER BY date DESC", location);
    }

    public List<AnonReservationDTO> findActiveAnonymised(User client) {
        return stream("date >= ?1", Date.valueOf(LocalDate.now()))
                .map(r -> new AnonReservationDTO(r, client))
                .toList();
    }

    public void setBilled(Reservation reservation, Boolean billed) {
        reservation.setBilled(billed);
        persistAndFlush(reservation);
    }
}
