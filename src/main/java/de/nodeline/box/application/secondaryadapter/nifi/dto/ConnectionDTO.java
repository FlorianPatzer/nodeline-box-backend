package de.nodeline.box.application.secondaryadapter.nifi.dto;


import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import de.nodeline.box.application.secondaryadapter.nifi.model.RelationshipInterface;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectionDTO {
    private String id;
    private String name;
    private ConnectableDTO source;
    private ConnectableDTO destination;
    private Set<String> selectedRelationships;

    public ConnectionDTO() {
        selectedRelationships = new HashSet<>();
    }

    public void addRelationship(RelationshipInterface relationship) throws JsonProcessingException {
        selectedRelationships.add(relationship.getValue());
    }
}

