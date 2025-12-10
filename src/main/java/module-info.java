module fryzjer {
    requires lombok;
    requires quarkus.hibernate.orm.panache;
    requires agroal.api;
    requires quarkus.core;
    requires microprofile.config.api;
    requires org.jboss.logging;
    requires quarkus.scheduler.api;
    requires jakarta.persistence;
    requires com.fasterxml.jackson.databind;
    requires jakarta.transaction;
    requires jakarta.el;
}