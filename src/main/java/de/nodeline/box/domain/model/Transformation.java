package de.nodeline.box.domain.model;

import java.util.List;

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

@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Or JOINED, if you prefer separate tables
@DiscriminatorColumn(name = "transformation_type", discriminatorType = DiscriminatorType.STRING)
@Entity
public class Transformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pipeline_id", referencedColumnName = "id")
    private Pipeline pipeline;
    
    private List<InputPort> inputPorts;
    private List<OutputPort> outputPorts;

    // Constructors, Getters, Setters

    public Transformation(Long id, Pipeline pipeline, List<InputPort> inputPorts, List<OutputPort> outputPorts) {
        this.id = id;
        this.pipeline = pipeline;
        this.inputPorts = inputPorts;
        this.outputPorts = outputPorts;
    }
   
}
