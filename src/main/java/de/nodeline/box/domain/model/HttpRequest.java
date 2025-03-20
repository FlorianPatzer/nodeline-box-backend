package de.nodeline.box.domain.model;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Validated
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode
public abstract class HttpRequest {
    @Id
    @JsonProperty("id")
    private UUID id;
    
    @OneToOne
    private Endpoint endpoint;

    @JsonProperty("url")
    private String url;
    
    protected HttpRequest() {
        this.id = UUID.randomUUID();
    }

}
