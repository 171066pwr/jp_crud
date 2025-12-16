package com.mycompany.app.tasks;

import io.agroal.api.AgroalDataSource;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@ApplicationScoped
public class SQLiteBackup {
    private static final Logger LOGGER = Logger.getLogger(SQLiteBackup.class.getName());
    private final AtomicBoolean executing = new AtomicBoolean(false);
    final boolean enabled;

    @ConfigProperty(name = "quarkus.datasource.jdbc.url")
    String jdbcUrl;

    @Inject
    AgroalDataSource dataSource;

    @Inject
    public SQLiteBackup(@ConfigProperty(name = "db.backup.enabled") boolean enabled) {
        this.enabled = enabled;
    }

    // Execute a backup every 10 seconds
    @Scheduled(delay=1, delayUnit=TimeUnit.SECONDS, every="${db.backup.period}")
    void scheduled() {
        if(enabled){backup();}
    }

    // Execute a backup during shutdown
    public void onShutdown(@Observes ShutdownEvent event) {
        if(enabled){backup();}
    }

    void backup() {
        if (executing.compareAndSet(false, true)) {
            try {
                String dbFile = jdbcUrl.substring("jdbc:sqlite:".length());
                var originalDbFilePath = Paths.get(dbFile);
                LOGGER.info("Starting DB backup for file: " + dbFile);
                var backupDbFilePath = originalDbFilePath.toAbsolutePath()
                        .getParent().resolve(originalDbFilePath.getFileName() + "_backup");
                try (var conn = dataSource.getConnection();
                     var stmt = conn.createStatement()) {
                    // Execute the backup
                    stmt.executeUpdate("backup to " + backupDbFilePath);
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to backup the database", e);
                }
                LOGGER.info("Backup of " + dbFile + " completed.");
            } finally {
                executing.set(false);
            }
        } else {
            LOGGER.info("Backup in progress.");
        }
    }
}