package de.nodeline.box.domain.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyDiscriminatorValue;
import org.hibernate.annotations.AnyKeyJavaClass;
import org.hibernate.annotations.Cascade;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@EqualsAndHashCode
public class DataSource {
    
    @Id
    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_ABSENT) // Exclude from JSON if absent
    @JsonSetter(nulls = Nulls.FAIL) // FAIL setting if the value is null
    @Setter
    private UUID id;

    @ManyToMany(mappedBy = "dataSources") // Reference to the `courses` field in Student
    @JsonProperty("pipelines")
    @Valid
    private Set<Pipeline> pipelines;

    @Any
    @AnyKeyJavaClass(UUID.class)
    @JoinColumn( name = "procurer_id" )
    @Column(name = "procurer_type")
    @AnyDiscriminatorValue(discriminator="http_get", entity=HttpGetRequest.class)
    @Setter
    @JsonProperty("procurer")
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @Valid
    private DataSourceInterface procurer;

    @OneToMany(mappedBy = "source", cascade = {CascadeType.ALL})
    @JsonProperty("out")
    @Valid
    private Set<Link> out;

    public DataSource() {
        this.id = UUID.randomUUID();
        this.out = new HashSet<>();
        this.pipelines = new HashSet<>();
    }

    public void addOut(Link outLink) {
        this.out.add(outLink);
    }

}
