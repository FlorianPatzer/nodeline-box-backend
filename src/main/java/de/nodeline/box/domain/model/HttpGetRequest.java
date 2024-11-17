package de.nodeline.box.domain.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("HTTP_GET") // Optional if using a single table for inheritance
public class HttpGetRequest extends DataSource {
    private String url;



    // Getters, Setters
}
