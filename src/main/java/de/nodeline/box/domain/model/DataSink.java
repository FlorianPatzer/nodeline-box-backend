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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Validated
@Data
@Entity
@Table(name="data_sink")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AllArgsConstructor
@EqualsAndHashCode(of={"id", "pipeline"})
public class DataSink {
    @Id
    @Setter
    private UUID id;

    private String name;
    
    private String description;

    @ManyToOne
    @JoinColumn(name = "pipeline_id", referencedColumnName = "id")
    @Valid
    private Pipeline pipeline;

    @Any
    @AnyKeyJavaClass(UUID.class)
    @JoinColumn( name = "deliverer_id" )
    @Column(name = "deliverer_type")
    @AnyDiscriminatorValue(discriminator="http_post", entity=HttpPostRequest.class)
    @Setter
    @Valid
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    private DataSinkInterface deliverer;    

    @OneToMany(mappedBy = "sink", cascade = {CascadeType.ALL})
    @Valid
    private Set<Link> in;

    public DataSink() {
        this.id = UUID.randomUUID();
        this.in = new HashSet<>();
    }

    public void addIn(PeerToPeerConnection in) {
        // in.setSink(this);
        this.in.add(in);
    }

}
