package de.nodeline.box.application.primaryadapter.nifi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessGroupDTO {
    private String id;
    private String name;
    private String parentGroupId;
    private PositionDTO position;
    private String comments;
}

