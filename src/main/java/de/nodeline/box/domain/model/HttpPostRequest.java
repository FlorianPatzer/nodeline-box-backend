package de.nodeline.box.domain.model;

import jakarta.persistence.Entity;

@Entity
public class HttpPostRequest extends HttpRequest implements DataSinkInterface {
    
    public HttpPostRequest() {
        super();
    }

    @Override
    public void placeData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'placeData'");
    }
}
