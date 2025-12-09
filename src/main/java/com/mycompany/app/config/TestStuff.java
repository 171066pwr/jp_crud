package com.mycompany.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.app.model.dao.ServiceRepository;
import io.agroal.api.AgroalDataSource;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.mycompany.app.model.entities.Service;

@ApplicationScoped
public class TestStuff {
    private static final Logger LOGGER = Logger.getLogger(SQLiteBackup.class.getName());

    @ConfigProperty(name = "quarkus.datasource.jdbc.url")
    String jdbcUrl;

    @Inject
    AgroalDataSource dataSource;

    private final AtomicBoolean executing = new AtomicBoolean(false);

    @Inject
    EntityManager entityManager;

    @Inject
    ServiceRepository serviceRepository;

    // Execute a backup every 10 seconds
    @Scheduled(delay=1, delayUnit=TimeUnit.SECONDS, every="1s")
    void scheduled() {
        Service service = persist();
        kopytko(service);
    }

    @Transactional
    public Service persist() {
        System.out.println("Persisting s1");
        Service service = new Service(null, "Kopytko", new BigDecimal("3.15"));
        writeAsString(service);
        entityManager.persist(service);
        return service;
    }

    @Transactional
    public void kopytko(Service service) {
        System.out.println("Reading s1");
        Service service2 = serviceRepository.findById(service.getId());
        writeAsString(service2);
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