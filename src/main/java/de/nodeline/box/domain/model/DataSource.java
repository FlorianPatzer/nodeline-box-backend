package de.nodeline.box.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyDiscriminatorValue;
import org.hibernate.annotations.AnyKeyJavaClass;
import org.hibernate.annotations.Cascade;
import org.springframework.validation.annotation.Validated;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;


@Entity
@Table(name="data_source")
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AllArgsConstructor
@Validated
@EqualsAndHashCode(of={"id", "pipeline"})
public class DataSource {
    
    @Id    
    @Setter
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "pipeline_id", referencedColumnName = "id")
    @Valid
    private Pipeline pipeline;

    @Any
    @AnyKeyJavaClass(UUID.class)
    @JoinColumn( name = "procurer_id" )
    @Column(name = "procurer_type")
    @AnyDiscriminatorValue(discriminator="http_get", entity=HttpGetRequest.class)
    @Setter
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @Valid
    private DataSourceInterface procurer;

    @OneToMany(mappedBy = "source", cascade = {CascadeType.ALL})
    @Valid
    private Set<Link> out;

    public DataSource() {
        this.id = UUID.randomUUID();
        this.out = new HashSet<>();
    }

    public void addOut(Link outLink) {
        this.out.add(outLink);
        outLink.setSource(this);
    }


}
