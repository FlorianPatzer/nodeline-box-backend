package de.nodeline.box.application.acl.nifi;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import de.nodeline.box.application.acl.WorkflowEngineService;
import de.nodeline.box.application.primaryadapter.nifi.NiFiService;
import de.nodeline.box.application.primaryadapter.nifi.dto.ProcessGroupDTO;
import de.nodeline.box.application.primaryadapter.nifi.dto.ProcessGroupEntity;
import de.nodeline.box.application.primaryadapter.nifi.model.ProcessGroup;
import de.nodeline.box.application.secondaryadapter.NifiProcessGroupRepositoryInterface;
import de.nodeline.box.domain.model.Pipeline;

@Service
public class NifiWorkflowEngineService implements WorkflowEngineService {
    @Autowired
    private NiFiService niFiService;
    @Autowired
    private NifiProcessGroupRepositoryInterface pgRepo;

    @Override
    public boolean createPipeline(Pipeline pipeline) {
        ProcessGroupDTO pgDto = new ProcessGroupDTO();
        pgDto.setId(null);
        pgDto.setName(pipeline.getId().toString());
        pgDto.setComments("Process Group for nodeline box pipeline " + pipeline.getId().toString());
        pgDto.setPosition(null);
        ResponseEntity<ProcessGroupEntity> response = niFiService.createProcessGroup(pgDto);
        if(response.getStatusCode() == HttpStatus.CREATED) {
            String nifiProcessGroupId = response.getBody().getComponent().getId();
            String nifiProcessGroupVersion = String.valueOf(response.getBody().getRevision().getVersion());
            ProcessGroup pgEntity = new ProcessGroup(nifiProcessGroupId, pipeline.getId(), nifiProcessGroupVersion);
            pgRepo.save(pgEntity);
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePipeline(Pipeline pipeline) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePipeline'");
    }

    @Override
    public boolean deletePipeline(UUID pipelineId) {
        ProcessGroup pgEntity = pgRepo.findByPipelineId(pipelineId);
        niFiService.deleteProcessGroup(pgEntity.getId(), pgEntity.getVersion());
        pgRepo.delete(pgEntity);
        return true;
    }

    public NiFiService getNiFiService( ) {
        return niFiService;
    }
}
