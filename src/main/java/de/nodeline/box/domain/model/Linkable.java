package de.nodeline.box.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

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
@EqualsAndHashCode
public abstract class Linkable {
    @Id
    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
    @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
    protected UUID id;

    @OneToMany(mappedBy = "in", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)    
    @JsonProperty("out")
    protected Set<Link> out;
    
    @OneToMany(mappedBy = "out", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @JsonProperty("in")
    protected Set<Link> in;
    
    @ManyToOne
    @JoinColumn(name = "pipeline_id", referencedColumnName = "id")    
    @JsonProperty("pipeline")
    private Pipeline pipeline;

    protected Linkable() {
        this.id = UUID.randomUUID();
        this.out = new HashSet<>();
        this.in = new HashSet<>();
    }
}
