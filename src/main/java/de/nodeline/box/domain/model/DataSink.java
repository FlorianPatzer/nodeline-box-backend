package de.nodeline.box.domain.model;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyDiscriminatorValue;
import org.hibernate.annotations.AnyKeyJavaClass;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Validated
@Data
@Entity
@Table(name="data_sink")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AllArgsConstructor
@EqualsAndHashCode
public class DataSink {
    @Id
    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
    @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
    @Setter
    private UUID id;

    @ManyToMany(mappedBy = "dataSinks") // Reference to the `courses` field in Student
    @JsonProperty("pipelines")
    @Valid
    private Set<Pipeline> pipelines;

    @Any
    @AnyKeyJavaClass(UUID.class)
    @JoinColumn( name = "deliverer_id" )
    @Column(name = "deliverer_type")
    @AnyDiscriminatorValue(discriminator="http_post", entity=HttpPostRequest.class)
    @Setter
    @JsonProperty("deliverer")
    @Valid
    private DataSinkInterface deliverer;    

    @OneToMany(mappedBy = "sink", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonProperty("in")
    @Valid
    private Set<Link> in;

    public DataSink() {
        this.id = UUID.randomUUID();
    }

}
