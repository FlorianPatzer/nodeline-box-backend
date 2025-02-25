package de.nodeline.box.application.secondaryadapter.nifi.dto;


import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.nodeline.box.application.secondaryadapter.nifi.model.RelationshipInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectionDTO {
    private String id;
    private String name;
    private ConnectableDTO source;
    private ConnectableDTO destination;
    private Set<RelationshipInterface> selectedRelationships;

}

