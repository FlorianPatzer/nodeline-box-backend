package de.nodeline.box.domain.model;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public class Pipeline {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "nodeline_box_id", referencedColumnName = "id")
    private NodelineBox nodelineBox;

    @ManyToMany
    @JoinTable(
        name = "pipeline_data_source", // Join table name
        joinColumns = @JoinColumn(name = "data_source_id"),
        inverseJoinColumns = @JoinColumn(name = "pipeline_id")
    )
    private Set<DataSource> dataSources;

    @ManyToMany
    @JoinTable(
        name = "pipeline_data_sink", // Join table name
        joinColumns = @JoinColumn(name = "data_sink_id"),
        inverseJoinColumns = @JoinColumn(name = "pipeline_id")
    )
    private Set<DataSink> dataSinks;

    @OneToMany(mappedBy = "pipeline", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transformation> transformations;

    public Pipeline() {
        this.id = UUID.randomUUID();
    }

}