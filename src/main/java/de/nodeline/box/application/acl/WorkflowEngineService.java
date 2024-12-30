package de.nodeline.box.application.acl;

import de.nodeline.box.domain.model.Pipeline;

public interface WorkflowEngineService {
    public void createPipeline(Pipeline pipeline);
    public void updatePipeline(Pipeline pipeline);
    public void deletePipeline(Pipeline pipeline);
}
