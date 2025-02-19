package de.nodeline.box.application.primaryadapter.api.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HttpGetRequestAttributesDto extends ProcurerAttributesDto {
    @JsonProperty("endpointId")
    private UUID endpointId;

    @JsonProperty("url")
    @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
    private String url;
}
