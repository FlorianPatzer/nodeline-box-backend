package de.nodeline.box.application.primaryadapter.nifi.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name="connection")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of={"id", "version", "modelId"})
public class Connection {
    @Id
    private String id; 
    String version;
    @ManyToOne
    @JoinColumn(name = "process_group_id", referencedColumnName = "id")
    ProcessGroup processGroup;
    //Back reference to the backend entity
    UUID modelId;
}