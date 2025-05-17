package de.nodeline.box.domain.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode(callSuper = true)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, // Use the class name as the type identifier
    include = JsonTypeInfo.As.PROPERTY, // Include the type info as a JSON property
    property = "type" // The property name in JSON to indicate the class type
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = JoltTransformation.class, name = "JoltTransformation") // Map JoltTransformation as a subtype
})
abstract public class Transformation extends Linkable {

    // Constructors, Getters, Setters

    public Transformation() {
        super();
    }

    public Transformation(Pipeline pipeline, Set<Link> in, Set<Link> out) {
        
    }
   
}
