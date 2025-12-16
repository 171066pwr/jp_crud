package com.mycompany.app.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private BigDecimal price;

    public Service(String name, BigDecimal price) {
        this(null, name, price);
    }
}
