package de.nodeline.box.domain.model;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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

    protected Linkable() {
        this.id = UUID.randomUUID();
    }
}
