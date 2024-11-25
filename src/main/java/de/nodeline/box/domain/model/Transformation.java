package de.nodeline.box.domain.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract public class Transformation extends Linkable {

    // Constructors, Getters, Setters

    public Transformation() {
        super();
    }

    public Transformation(Pipeline pipeline, Set<Link> in, Set<Link> out) {
        
    }
   
}
