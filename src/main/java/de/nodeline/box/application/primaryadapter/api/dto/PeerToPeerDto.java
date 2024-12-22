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
public class PeerToPeerDto {
    @JsonProperty("id")
    @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
    private UUID id;
    @JsonProperty("sourceLinkableRef")
    protected LinkableReferenceDto sourceLinkableRef;
    @JsonProperty("targetLinkableRef")
    protected LinkableReferenceDto targetLinkableRef;
    @JsonProperty("sinkId")
    protected UUID sinkId;
    @JsonProperty("sourceId")
    protected UUID sourceId;
    @JsonProperty("pipelineId")
    private UUID pipelineId;
}
