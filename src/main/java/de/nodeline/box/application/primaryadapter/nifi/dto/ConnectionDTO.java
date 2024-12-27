package de.nodeline.box.application.primaryadapter.nifi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionDTO {
    private String id;
    private String name;
    private String sourceId;
    private String sourceType;
    private String destinationId;
    private String destinationType;
    private PositionDTO position;
}

