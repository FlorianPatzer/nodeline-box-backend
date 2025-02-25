package de.nodeline.box.application.primaryadapter.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PipelineStatusDto {
    @JsonProperty("RUNNING")
    RUNNING,
    @JsonProperty("STOPPED")
    STOPPED,
    @JsonProperty("ISSUE_EXISTS")
    ISSUE_EXISTS,
    @JsonProperty("NOT_DEPLOYED")
    NOT_DEPLOYED
}
