package com.mycompany.app.model.dao;

import com.mycompany.app.model.entities.Location;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LocationRepository implements PanacheRepository<Location> {
}
