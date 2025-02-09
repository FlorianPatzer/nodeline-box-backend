package de.nodeline.box.domain.model;

public enum PipelineStatus {
    // Pipeline is deployed and running
    RUNNING,
    // Pipeline is deployed and stopped
    STOPPED,
    // An issue exists with the pipeline
    ISSUE_EXISTS,
    // Pipeline is not deployed yet
    NOT_DEPLOYED
}
