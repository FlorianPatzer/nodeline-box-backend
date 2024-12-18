package de.nodeline.box.application.primaryadapter.api.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSinkDto {
    @JsonProperty("id")
    @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
    private UUID id;
    @JsonProperty("pipelineIds")
    private Set<UUID> pipelineIds;
    @JsonProperty("deliverer")
    private DelivererDto deliverer;
    @JsonProperty("inboundLinks")
    private Set<LinkDto> inboundLinks;

    public DataSinkDto() {
        pipelineIds = new HashSet<>();
        inboundLinks = new HashSet<>();
    }

    public void addInboundLink(LinkDto link) {
        this.inboundLinks.add(link);
    }

    public void addPipelineId(UUID id) {
        this.pipelineIds.add(id);
    }
}
