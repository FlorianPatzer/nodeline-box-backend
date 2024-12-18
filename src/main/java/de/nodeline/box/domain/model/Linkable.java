package de.nodeline.box.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Validated
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode(of={"id", "pipeline"})
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, // Use the class name as the type identifier
    include = JsonTypeInfo.As.PROPERTY, // Include the type info as a JSON property
    property = "type" // The property name in JSON to indicate the class type
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Transformation.class, name = "Transformation") // Map Transformation as a subtype
})
public abstract class Linkable {
    @Id
    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
    @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
    protected UUID id;

    @OneToMany(mappedBy = "in", cascade = {CascadeType.ALL}, orphanRemoval = true)    
    @JsonProperty("out")
    protected Set<Link> out;
    
    @OneToMany(mappedBy = "out", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JsonProperty("in")
    protected Set<Link> in;
    
    @ManyToOne
    @JoinColumn(name = "pipeline_id", referencedColumnName = "id")    
    @JsonProperty("pipeline")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Pipeline pipeline;

    protected Linkable() {
        this.id = UUID.randomUUID();
        this.out = new HashSet<>();
        this.in = new HashSet<>();
    }

    public void addIn(Link in) {
        this.in.add(in);
    }
    public void addOut(Link out) {
        this.out.add(out);
    }
}
