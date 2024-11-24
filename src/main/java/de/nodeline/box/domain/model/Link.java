package de.nodeline.box.domain.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Link {
    
    @Id
    protected UUID id;
    @ManyToOne
    protected Linkable in;
    @ManyToOne
    protected Linkable out;
    @ManyToOne
    protected DataSink sink;
    @ManyToOne
    protected DataSource source;

    protected Link() {
        this.id = UUID.randomUUID();
    }

}
