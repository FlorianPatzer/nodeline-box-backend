package de.nodeline.box.domain.model;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public class Device {
    @Id
    private UUID id;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Endpoint> endpoints;

    public Device() {
        this.id = UUID.randomUUID();
    }

    // Getters, Setters
}
