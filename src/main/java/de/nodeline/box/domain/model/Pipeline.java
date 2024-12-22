package de.nodeline.box.domain.model;

import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

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
@EqualsAndHashCode(of={"id", "nodelineBox", "linkables", "links"})
@Validated
@Data
public class Pipeline {
    @Id
    @Setter
    @Getter
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "nodeline_box_id", referencedColumnName = "id")
    @Valid
    private NodelineBox nodelineBox;

    @OneToMany(mappedBy = "pipeline", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @Setter
    @Valid
    private Set<DataSource> dataSources;

    @OneToMany(mappedBy = "pipeline", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @Setter
    @Getter
    @Valid
    private Set<DataSink> dataSinks;

    @OneToMany(mappedBy = "pipeline", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @Setter
    @Getter    
    @Valid
    private Set<Linkable> linkables;
    
    @OneToMany(mappedBy = "pipeline", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @Setter
    @Getter    
    @Valid
    private Set<Link> links;

    public Pipeline() {
        this.id = UUID.randomUUID();
    }

    public void addDataSource(DataSource dataSource) {
        this.dataSources.add(dataSource);
    }

    public void addDataSink(DataSink dataSink) {
        this.dataSinks.add(dataSink);
    }

    public void addLinkable(Linkable linkable) {
        this.linkables.add(linkable);
    }

    public void addLink(Link link) {
        this.links.add(link);
    }
    
}