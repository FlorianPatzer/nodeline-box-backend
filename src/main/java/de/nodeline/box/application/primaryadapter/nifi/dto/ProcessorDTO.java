package de.nodeline.box.application.primaryadapter.nifi.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessorDTO {
    private String id;
    private String name;
    private String type;
    private PositionDTO position;
    private ConfigDTO config;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ConfigDTO {
    private Map<String, String> properties;
    private boolean autoTerminatedRelationships;
}
