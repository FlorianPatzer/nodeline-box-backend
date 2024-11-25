package de.nodeline.box.domain.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Endpoint {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    private Device device;

    public Endpoint() {
        this.id = UUID.randomUUID();
    }

    // Getters, Setters
}
