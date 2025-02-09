package de.nodeline.box.domain.model;

public enum EngineFlowStatus {
    // The requested flow was not found
    NOT_FOUND,
    // An issue exists
    ISSUE_EXISTS,
    // A flow has been successfully deleted
    DELETED,
    // The flow is deployed and running
    RUNNING,
    // The flow is deployed and stopped
    STOPPED
}
