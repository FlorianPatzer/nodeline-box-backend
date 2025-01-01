package de.nodeline.box.application.acl;

import java.util.UUID;

import de.nodeline.box.domain.model.Pipeline;

public interface WorkflowEngineService {
    public boolean createPipeline(Pipeline pipeline);
    public boolean updatePipeline(Pipeline pipeline);
    public boolean deletePipeline(UUID pipelineId);
}
