package de.nodeline.box.application.secondaryadapter.nifi.dto;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.nodeline.box.application.secondaryadapter.nifi.model.RelationshipInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigDTO {
    private Map<String, String> properties;
    @JsonProperty("autoTerminatedRelationships")
    private Set<RelationshipInterface> autoTerminatedRelationships;
}