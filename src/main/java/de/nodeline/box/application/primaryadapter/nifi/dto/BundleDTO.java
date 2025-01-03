package de.nodeline.box.application.primaryadapter.nifi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BundleDTO {
    private String group;
    private String artifact;
    private String version;
}
