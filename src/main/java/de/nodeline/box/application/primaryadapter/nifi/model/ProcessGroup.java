package de.nodeline.box.application.primaryadapter.nifi.model;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name="process_group")
@AllArgsConstructor
@Data
public class ProcessGroup {
    @Id
    private String id; 
    UUID pipelineId;
    String version;

    @OneToMany(mappedBy = "processGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Processor> processors;

    @OneToMany(mappedBy = "processGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Connection> connections;


    public ProcessGroup() {
        processors = new HashSet<>();
    }

    public void addProcessor(Processor processorEntity) {
        processors.add(processorEntity);
    }

    public void addConnection(Connection connectionEntity) {
        connections.add(connectionEntity);
    }
}
