package de.nodeline.box.application.acl.nifi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.nodeline.box.application.acl.WorkflowEngineService;
import de.nodeline.box.application.primaryadapter.nifi.NiFiService;
import de.nodeline.box.application.primaryadapter.nifi.dto.ProcessGroupDTO;
import de.nodeline.box.domain.model.Pipeline;

@Service
public class NifiWorkflowEngineService implements WorkflowEngineService {
    @Autowired
    private NiFiService niFiService;

    @Override
    public void createPipeline(Pipeline pipeline) {
        ProcessGroupDTO pgDto = new ProcessGroupDTO();
        pgDto.setName(pipeline.getId().toString());
        pgDto.setComments("Process Group for nodeline box pipeline " + pipeline.getId().toString());
        pgDto.setPosition(null);
        niFiService.createProcessGroup(pgDto);
    }

    @Override
    public void updatePipeline(Pipeline pipeline) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePipeline'");
    }

    @Override
    public void deletePipeline(Pipeline pipeline) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePipeline'");
    }

    public NiFiService getNiFiService( ) {
        return niFiService;
    }
}
