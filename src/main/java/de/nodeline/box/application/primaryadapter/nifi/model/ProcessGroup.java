package de.nodeline.box.application.primaryadapter.nifi.model;


import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="process_group")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProcessGroup {
    @Id
    private String id; 
    UUID pipelineId;
    String version;

}
