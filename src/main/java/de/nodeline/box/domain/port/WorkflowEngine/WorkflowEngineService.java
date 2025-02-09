package de.nodeline.box.domain.port.WorkflowEngine;

import java.util.UUID;

import de.nodeline.box.domain.model.Pipeline;

public interface WorkflowEngineService {
    public EngineResponse createFlow(Pipeline pipeline);
    public EngineResponse updateFlow(Pipeline pipeline);
    public EngineResponse deleteFlow(UUID pipelineId);
}
