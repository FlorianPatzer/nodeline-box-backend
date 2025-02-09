package de.nodeline.box.application.secondaryadapter.nifi.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessorDTO {
    private String id;
    private String name;
    private String type;
    private BundleDTO bundle;
    private PositionDTO position;
    private ConfigDTO config;

}