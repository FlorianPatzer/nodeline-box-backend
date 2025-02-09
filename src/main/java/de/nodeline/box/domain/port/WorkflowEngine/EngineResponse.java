package de.nodeline.box.domain.port.WorkflowEngine;

import de.nodeline.box.domain.model.EngineFlowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EngineResponse {
    private EngineFlowStatus status;
    private String issueMessage;

    public EngineResponse(EngineFlowStatus status) {
        this.status = status;
    }
}
