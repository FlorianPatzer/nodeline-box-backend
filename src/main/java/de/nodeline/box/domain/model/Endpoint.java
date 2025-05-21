package de.nodeline.box.domain.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "endpoint", cascade = {CascadeType.ALL})
    @Valid
    private Set<Credentials> credentials;

    public Endpoint() {
        this.id = UUID.randomUUID();
        this.credentials = new HashSet<>();
    }

    public void addCredentials(Credentials credentials) {
        credentials.setEndpoint(this);
        this.credentials.add(credentials);
    }
}
