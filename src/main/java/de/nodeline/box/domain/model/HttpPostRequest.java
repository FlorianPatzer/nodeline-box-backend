package de.nodeline.box.domain.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("HTTP_POST") // Optional if using a single table for inheritance
public class HttpPostRequest extends DataSink {
    private String url;
    

    // Getters, Setters
}
