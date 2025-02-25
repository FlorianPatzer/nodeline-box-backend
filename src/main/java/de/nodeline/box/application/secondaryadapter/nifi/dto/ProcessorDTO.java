package de.nodeline.box.application.secondaryadapter.nifi.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import de.nodeline.box.application.secondaryadapter.nifi.model.Processor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessorDTO {
    private String id;
    private String name;
    private Processor.Type type;
    private BundleDTO bundle;
    private PositionDTO position;
    private ConfigDTO config;

}