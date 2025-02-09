package de.nodeline.box.application.secondaryadapter.nifi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectableDTO {    
    private String id;
    private Type type;
    private String groupId;

    public enum Type {
        PROCESSOR,
        // REMOTE_INPUT_PORT,
        // REMOTE_OUTPUT_PORT,
        // INPUT_PORT,
        // OUTPUT_PORT,FUNNEL
    }
}
