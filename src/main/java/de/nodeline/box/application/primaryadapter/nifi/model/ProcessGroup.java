package de.nodeline.box.application.primaryadapter.nifi.model;

import java.util.UUID;

import de.nodeline.box.domain.model.Pipeline;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Setter;

@Entity
@Table(name="process_group")
public class ProcessGroup {
    @Id
    @Setter
    private UUID id;
 
    @OneToOne
    Pipeline pipeline;

}
