package de.nodeline.box.domain.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class HttpRequest {
    @Id
    private UUID id;
    
    @OneToOne
    @JoinColumn(name = "endpoint_id", referencedColumnName = "id")
    private Endpoint endpoint;

    private String url;
    
    protected HttpRequest() {
        this.id = UUID.randomUUID();
    }
}
