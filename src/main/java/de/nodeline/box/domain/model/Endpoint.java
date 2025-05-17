package de.nodeline.box.domain.model;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

/*
 * Endpoint, e.g. an api. objects of this class can contain credentials, headers and more.
 */

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Validated
@EqualsAndHashCode
public abstract class Endpoint {
    @Id
    @Setter
    private UUID id;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    private Device device;

    public Endpoint() {
        this.id = UUID.randomUUID();
    }
}
