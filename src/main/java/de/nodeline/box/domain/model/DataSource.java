package de.nodeline.box.domain.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Or JOINED for separate tables
@DiscriminatorColumn(name = "data_source_type", discriminatorType = DiscriminatorType.STRING)
@Entity
public class DataSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "pipeline_id", referencedColumnName = "id")
    private Pipeline pipeline;
    
    @OneToOne
    @JoinColumn(name = "endpoint_id", referencedColumnName = "id")
    private Endpoint endpoint;

    public DataSource(Long id, Pipeline pipeline, Endpoint endpoint) {
        this.id = id;
        this.pipeline = pipeline;
        this.endpoint = endpoint;
    }

    // Getters, Setters
}
