package de.nodeline.box.domain.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class NodelineBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "nodelineBox", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pipeline> pipelines;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    
    // Constructors, Getters, Setters
    public NodelineBox(Long id, User user) {
        this.id = id;
        this.user = user;
    }

    public void addPipeline(Pipeline pipeline) {
        pipelines.add(pipeline);
    }
}
