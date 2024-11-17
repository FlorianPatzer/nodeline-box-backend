package de.nodeline.box.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class DataSink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pipeline_id", referencedColumnName = "id")
    private Pipeline pipeline;

    @OneToOne
    @JoinColumn(name = "endpoint_id", referencedColumnName = "id")
    private Endpoint endpoint;

    public DataSink(String url, Long id, Pipeline pipeline, Endpoint endpoint) {
        this.id = id;
        this.pipeline = pipeline;
        this.endpoint = endpoint;
    }

    // Getters, Setters
}
