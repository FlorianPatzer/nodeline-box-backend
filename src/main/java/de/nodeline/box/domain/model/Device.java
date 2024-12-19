package de.nodeline.box.domain.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Data
@AllArgsConstructor
@Validated
@EqualsAndHashCode
public class Device {
    @Id
    @Setter
    private UUID id;

    @OneToMany(mappedBy = "device", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @Valid
    private Set<Endpoint> endpoints;

    public Device() {
        this.id = UUID.randomUUID();
        this.endpoints = new HashSet<>();
    }

    public void addEndpoint(Endpoint endpoint) {
        this.endpoints.add(endpoint);
    }
}
