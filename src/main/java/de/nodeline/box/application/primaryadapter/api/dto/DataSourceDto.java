package de.nodeline.box.application.primaryadapter.api.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSourceDto {
    @JsonProperty("id")
    @JsonSetter(nulls = Nulls.FAIL) // FAIL setting if the value is null
    private UUID id;
    @JsonProperty("pipelineIds")
    private Set<UUID> pipelineIds;
    @JsonProperty("procurer")
    private ProcurerDto procurer;
    @JsonProperty("outboundLinks")
    private Set<LinkDto> outboundLinks;

    public DataSourceDto() {
        pipelineIds = new HashSet<>();
        outboundLinks = new HashSet<>();
    }

    public void addOutboundLink(LinkDto link) {
        this.outboundLinks.add(link);
    }

    public void addPipelineId(UUID id) {
        this.pipelineIds.add(id);
    }
}
