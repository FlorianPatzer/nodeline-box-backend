package de.nodeline.box.application.secondaryadapter.nifi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessGroupEntity {
    private RevisionDTO revision;
    private ProcessGroupDTO component;
}
