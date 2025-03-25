package de.nodeline.box.domain.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Entity
public class RestEndpoint extends Endpoint {
    private String baseUrl;

    public RestEndpoint() {
        super();
    }
}
