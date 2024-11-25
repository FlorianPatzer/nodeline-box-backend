package de.nodeline.box.domain.model;

import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyDiscriminatorValue;
import org.hibernate.annotations.AnyKeyJavaClass;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Setter;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AllArgsConstructor
public class DataSource {
    
    @Id
    private UUID id;

    @ManyToMany(mappedBy = "dataSources") // Reference to the `courses` field in Student
    private Set<Pipeline> pipelines;

    @Any
    @AnyKeyJavaClass(UUID.class)
    @JoinColumn( name = "procurer_id" )
    @Column(name = "procurer_type")
    @AnyDiscriminatorValue(discriminator="http_get", entity=HttpGetRequest.class)
    @Setter
    private DataSourceInterface procurer;

    @OneToMany(mappedBy = "source")
    private Set<Link> out;

    public DataSource() {
        this.id = UUID.randomUUID();
    }

}
