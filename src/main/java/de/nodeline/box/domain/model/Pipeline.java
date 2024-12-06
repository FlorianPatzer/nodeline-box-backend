package de.nodeline.box.domain.model;

import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@AllArgsConstructor
@EqualsAndHashCode
@Validated
@Data
public class Pipeline {
    @Id
    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
    @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
    @Setter
    @Getter
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "nodeline_box_id", referencedColumnName = "id")
    @JsonProperty("nodeline-box")
    @Valid
    private NodelineBox nodelineBox;

    @ManyToMany
    @JoinTable(
        name = "pipeline_data_source", // Join table name
        joinColumns = @JoinColumn(name = "data_source_id"),
        inverseJoinColumns = @JoinColumn(name = "pipeline_id")
    )
    @Setter
    @JsonProperty("dataSources")
    @Valid
    private Set<DataSource> dataSources;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(
        name = "pipeline_data_sink", // Join table name
        joinColumns = @JoinColumn(name = "data_sink_id"),
        inverseJoinColumns = @JoinColumn(name = "pipeline_id")
    )
    @Setter
    @Getter
    @JsonProperty("dataSinks")
    @Valid
    private Set<DataSink> dataSinks;

    @OneToMany(mappedBy = "pipeline", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @Setter
    @Getter    
    @JsonProperty("linkables")
    @Valid
    private Set<Linkable> linkables;
    
    @OneToMany(mappedBy = "pipeline", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @Setter
    @Getter    
    @JsonProperty("links")
    @Valid
    private Set<Link> links;

    public Pipeline() {
        this.id = UUID.randomUUID();
    }

}