package de.nodeline.box.domain.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Validated
@EqualsAndHashCode
public class Endpoint {
    @Id
    @Setter
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    private Device device;

    public Endpoint() {
        this.id = UUID.randomUUID();
    }
}
