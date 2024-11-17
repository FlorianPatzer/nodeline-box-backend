package de.nodeline.box.domain.model;

import jakarta.persistence.*;

@Entity
public class PeerToPeerConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "input_port_id", referencedColumnName = "id")
    private InputPort inputPort;

    @ManyToOne
    @JoinColumn(name = "output_port_id", referencedColumnName = "id")
    private OutputPort outputPort;

    public PeerToPeerConnection(Long id, InputPort inputPort, OutputPort outputPort) {
        this.id = id;
        this.inputPort = inputPort;
        this.outputPort = outputPort;
    }
}
