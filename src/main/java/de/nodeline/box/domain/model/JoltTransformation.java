package de.nodeline.box.domain.model;

import org.springframework.validation.annotation.Validated;

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
