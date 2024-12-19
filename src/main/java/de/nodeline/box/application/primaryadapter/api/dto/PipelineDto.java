package de.nodeline.box.application.primaryadapter.api.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PipelineDto {    
    @JsonProperty("id")
    @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
    private UUID id;
    @JsonProperty("dataSources")
    private Set<DataSourceDto> dataSources;
    @JsonProperty("dataSinks")
    private Set<DataSinkDto> dataSinks;
    @JsonProperty("linkables")
    private Set<LinkableDto> linkables;
    @JsonProperty("links")
    private Set<PeerToPeerDto> links;

    public PipelineDto() {
        this.dataSinks = new HashSet<>();
        this.dataSources = new HashSet<>();
        this.linkables = new HashSet<>();
        this.links = new HashSet<>();
    }

    public void addDataSource(DataSourceDto dataSource) {
        this.dataSources.add(dataSource);
    }

    public void addDataSink(DataSinkDto dataSink) {
        this.dataSinks.add(dataSink);
    }

    public void addLinkable(LinkableDto linkable) {
        this.linkables.add(linkable);
    }

    public void addLink(PeerToPeerDto link) {
        this.links.add(link);
    }
}
