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
    @JsonProperty("pipelineId")
    private UUID pipelineId;
    @JsonProperty("procurer")
    private ProcurerDto procurer;
    @JsonProperty("outboundLinkIds")
    private Set<UUID> outboundLinkIds;

    public DataSourceDto() {
        outboundLinkIds = new HashSet<>();
    }

    public void addOutboundLinkId(UUID linkId) {
        this.outboundLinkIds.add(linkId);
    }
}
