package de.nodeline.box.application.secondaryadapter.nifi.dto;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;

import de.nodeline.box.application.secondaryadapter.nifi.model.RelationshipInterface;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigDTO {
    private Map<String, String> properties;
    @JsonProperty("autoTerminatedRelationships")
    private Set<String> autoTerminatedRelationships;

    public ConfigDTO() {
        autoTerminatedRelationships = new HashSet<>();
    }

    public void addRelationshipForTermination(RelationshipInterface relationship) throws JsonProcessingException {
        autoTerminatedRelationships.add(relationship.getValue());
    }
}