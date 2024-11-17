package de.nodeline.box.domain.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("JOLT") // Optional if using a single table for inheritance
public class JoltTransformation extends Transformation {
    private String joltSpecification;

    public JoltTransformation(Long id, String joltSpecification) {
        super(id);
        this.joltSpecification = joltSpecification;
    }

    // Getters, Setters
}
