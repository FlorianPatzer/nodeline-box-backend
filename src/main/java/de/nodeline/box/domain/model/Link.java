package de.nodeline.box.domain.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Link {
    
    @Id
    protected UUID id;

    @ManyToOne
    @Setter
    protected Linkable in;
    @ManyToOne
    @Setter
    protected Linkable out;

    @ManyToOne
    @Setter
    protected DataSink sink;
    @ManyToOne
    @Setter
    protected DataSource source;
    
    @ManyToOne
    @JoinColumn(name = "pipeline_id", referencedColumnName = "id")
    private Pipeline pipeline;

    protected Link() {
        this.id = UUID.randomUUID();
    }

}
