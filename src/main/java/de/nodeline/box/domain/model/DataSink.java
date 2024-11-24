package de.nodeline.box.domain.model;

import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyDiscriminatorValue;
import org.hibernate.annotations.AnyKeyJavaClass;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AllArgsConstructor
public abstract class DataSink {
    @Id
    private UUID id;

    @ManyToMany(mappedBy = "dataSinks") // Reference to the `courses` field in Student
    private Set<Pipeline> pipelines;

    @Any
    @AnyKeyJavaClass(UUID.class)
    @JoinColumn( name = "deliverer_id" )
    @Column(name = "deliverer_type")
    @AnyDiscriminatorValue(discriminator="http_post", entity=HttpPostRequest.class)
    private Set<DataSourceInterface> deliverers;    

    @OneToMany(mappedBy = "sink")
    protected Set<Link> in;

    public DataSink() {
        this.id = UUID.randomUUID();
    }

    // Getters, Setters
}
