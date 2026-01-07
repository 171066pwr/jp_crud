package com.mycompany.app.model.entities;

import io.quarkus.security.jpa.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.quarkus.elytron.security.common.BcryptUtil;

@Entity
@UserDefinition
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Username
    private String name;
    @Password(PasswordType.CLEAR)
    private String password;
    @Roles
    private String role;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Location location;

    public User(String name, String password, Role role, Location location) {
        this(null, name, password, role.toString(), location);
    }

    public User(String name, String password, String role, Location location) {
        this(null, name, password, role, location);
    }
}
