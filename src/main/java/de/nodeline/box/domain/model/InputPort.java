package de.nodeline.box.domain.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class InputPort {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transformation_id", referencedColumnName = "id")
    private Transformation transformation;

    @OneToMany(mappedBy = "inputPort", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PeerToPeerConnection> peerToPeerConnections;


    public InputPort(Long id, Transformation transformation, List<PeerToPeerConnection> peerToPeerConnections) {
        this.id = id;
        this.transformation = transformation;
        this.peerToPeerConnections = peerToPeerConnections;
    }

    // Getters, Setters
}
