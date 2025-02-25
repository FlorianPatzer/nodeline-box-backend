package de.nodeline.box.domain.model;

import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Validated
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JoltTransformation extends Transformation {

    @Column(length =  Integer.MAX_VALUE)
    private String joltSpecification;

    public JoltTransformation() {
        super();
    }
    
}
