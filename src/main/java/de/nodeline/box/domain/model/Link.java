package de.nodeline.box.domain.model;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Validated
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode
public abstract class Link {
    
    @Id
    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
    @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
    @Setter
    protected UUID id;

    @ManyToOne
    @Setter
    @JsonProperty("in")
    protected Linkable in;
    
    @ManyToOne
    @Setter
    @JsonProperty("out")
    protected Linkable out;

    @ManyToOne
    @Setter
    @JsonProperty("sink")
    protected DataSink sink;

    @ManyToOne
    @Setter
    @JsonProperty("source")
    protected DataSource source;
    
    @ManyToOne
    @JoinColumn(name = "pipeline_id", referencedColumnName = "id")
    @JsonProperty("pipeline")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
    @JsonSetter(nulls = Nulls.FAIL)
    private Pipeline pipeline;

    protected Link() {
        this.id = UUID.randomUUID();
    }
}
