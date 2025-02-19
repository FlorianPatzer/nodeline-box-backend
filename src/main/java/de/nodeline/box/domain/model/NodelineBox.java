package de.nodeline.box.domain.model;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class NodelineBox {
    @Id
    private UUID id;

    @OneToMany(mappedBy = "nodelineBox", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<Pipeline> pipelines;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private NodelineUser user;
    
    public NodelineBox() {
        this.id = UUID.randomUUID();
    }

    public void addPipeline(Pipeline pipeline) {
        pipelines.add(pipeline);
    }
}
