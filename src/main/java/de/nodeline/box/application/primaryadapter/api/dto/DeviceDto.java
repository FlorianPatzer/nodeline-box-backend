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
public class DeviceDto {

    @JsonProperty("id")
    @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
    private UUID id;

    @JsonProperty("endpoints")
    private Set<EndpointDto> endpoints;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    public DeviceDto(){
        this.endpoints = new HashSet<EndpointDto>();
    }

    public void addEndpoint(EndpointDto endpointDto) {
        this.endpoints.add(endpointDto);
    }
}
