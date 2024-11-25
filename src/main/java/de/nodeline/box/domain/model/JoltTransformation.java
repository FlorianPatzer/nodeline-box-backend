package de.nodeline.box.domain.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public class JoltTransformation extends Transformation {

    private String joltSpecification;

    public JoltTransformation() {
        super();
    }
    // Getters, Setters
}
