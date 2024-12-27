package de.nodeline.box.application.primaryadapter.nifi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessGroupDTO {
    private String id;
    private String name;
    private String parentGroupId;
    private PositionDTO position;
    private String comments;
}

