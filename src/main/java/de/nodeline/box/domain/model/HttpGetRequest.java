package de.nodeline.box.domain.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class HttpGetRequest extends HttpRequest implements DataSourceInterface {

    public HttpGetRequest() {
        super();
    }

    @Override
    public Record getData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getData'");
    }

    // Getters, Setters
}
