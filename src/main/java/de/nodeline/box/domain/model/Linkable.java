package de.nodeline.box.domain.model;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Linkable {
    @Id
    protected UUID id;

    @OneToMany(mappedBy = "in")
    protected Set<Link> out;
    
    @OneToMany(mappedBy = "out")
    protected Set<Link> in;
    
    @ManyToOne
    @JoinColumn(name = "pipeline_id", referencedColumnName = "id")
    private Pipeline pipeline;

    protected Linkable() {
        this.id = UUID.randomUUID();
    }
}
