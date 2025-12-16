package com.mycompany.app.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int slots;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private User owner;

    public Location(String name, int slots, User owner) {
        this(null, name, slots, owner);
    }
}
