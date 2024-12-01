package de.nodeline.box.domain.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity
@Validated
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JoltTransformation extends Transformation {

    @JsonProperty("jolt-spec")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
    @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
    private String joltSpecification;

    public JoltTransformation() {
        super();
    }
    
}
