package de.nodeline.box.domain.model;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Validated
@EqualsAndHashCode
public abstract class Credentials {
    @Id
    @Setter
    private UUID id;
    private String name;
    private String description;
    @ManyToOne
    private Endpoint endpoint;
    
    public Credentials() {
        this.id = UUID.randomUUID();
    }
}
