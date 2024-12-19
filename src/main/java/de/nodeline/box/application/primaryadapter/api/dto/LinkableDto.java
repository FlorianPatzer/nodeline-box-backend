package de.nodeline.box.application.primaryadapter.api.dto;

import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkableDto {
    @JsonProperty("id")
    @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
    private UUID id;
    @JsonProperty("type")  
    private Type type;   
    @JsonProperty("inboundLinkIds")  
    private Set<UUID> inboundLinkIds;    
    @JsonProperty("outboundLinkIds")    
    private Set<UUID> outboundLinkIds;    
    @JsonProperty("attributes")    
    private LinkableAttributesDto attributes;    
    @JsonProperty("pipelineId")
    private UUID pipelineId;

    public void addInboundLinkId(UUID linkId) {
        this.inboundLinkIds.add(linkId);
    }
    
    public void addOutboundLinkId(UUID linkId) {
        this.outboundLinkIds.add(linkId);
    }

    public enum Type {
        JOLT_TRANSFORMATION
    }
}
