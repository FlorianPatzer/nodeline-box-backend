package de.nodeline.box.application.acl.nifi;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;

import de.nodeline.box.application.acl.WorkflowEngineService;
import de.nodeline.box.application.primaryadapter.nifi.NiFiService;
import de.nodeline.box.application.primaryadapter.nifi.NifiTransformationService;
import de.nodeline.box.application.primaryadapter.nifi.dto.ConnectableDTO;
import de.nodeline.box.application.primaryadapter.nifi.dto.ConnectionDTO;
import de.nodeline.box.application.primaryadapter.nifi.dto.ConnectionEntity;
import de.nodeline.box.application.primaryadapter.nifi.dto.ProcessGroupDTO;
import de.nodeline.box.application.primaryadapter.nifi.dto.ProcessGroupEntity;
import de.nodeline.box.application.primaryadapter.nifi.dto.ProcessorDTO;
import de.nodeline.box.application.primaryadapter.nifi.dto.ProcessorEntity;
import de.nodeline.box.application.primaryadapter.nifi.dto.ConnectionDTO.Relationship;
import de.nodeline.box.application.primaryadapter.nifi.model.Connection;
import de.nodeline.box.application.primaryadapter.nifi.model.ProcessGroup;
import de.nodeline.box.application.primaryadapter.nifi.model.Processor;
import de.nodeline.box.application.secondaryadapter.NifiProcessGroupRepositoryInterface;
import de.nodeline.box.domain.model.Link;
import de.nodeline.box.domain.model.Pipeline;
import io.netty.handler.codec.http2.Http2Exception;

@Service
public class NifiWorkflowEngineService implements WorkflowEngineService {
    @Autowired
    private NiFiService niFiService;
    @Autowired
    private NifiProcessGroupRepositoryInterface pgRepo;
    @Autowired
    private NifiTransformationService transformationService;

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
            ProcessGroup pgEntity = new ProcessGroup(nifiProcessGroupId, pipeline.getId(), nifiProcessGroupVersion, new HashSet<>(), new HashSet<>());
            pipeline.getLinkables().forEach(linkable -> {
                ProcessorDTO processorDTO = transformationService.linkableToProcessorDTO(linkable);
                pgEntity.addProcessor(createProcessor(pgEntity, processorDTO, linkable.getId()));
            });
            pipeline.getDataSinks().forEach(sink -> {
                ProcessorDTO processorDTO = transformationService.sinkToProcessorDTO(sink);
                pgEntity.addProcessor(createProcessor(pgEntity, processorDTO, sink.getId()));
            });
            pipeline.getDataSources().forEach(source -> {
                ProcessorDTO processorDTO = transformationService.sourceToProcessorDTO(source);
                pgEntity.addProcessor(createProcessor(pgEntity, processorDTO, source.getId()));
            });
            pipeline.getLinks().forEach(link -> {                
                ConnectionDTO connectionDTO = transformationService.linkToConnectionDTO(
                    link,
                    getSourceProcessorByModelId(link,  pgEntity.getProcessors()).getId(),
                    getDestinationProcessorByModelId(link,  pgEntity.getProcessors()).getId(),
                    nifiProcessGroupId
                );
                pgEntity.addConnection(createConnection(pgEntity, connectionDTO, link.getId()));
            });
            pgRepo.save(pgEntity);
            return true;
        }
        return false;
    }

    private Connection createConnection(ProcessGroup processGroup, ConnectionDTO connectionDTO, UUID modelId) {
        ResponseEntity<ConnectionEntity> connectionDtoResponse = niFiService.createConnection(processGroup.getId(), connectionDTO);
        if(connectionDtoResponse.getStatusCode() == HttpStatus.CREATED) {
            Connection entity = new Connection(
                connectionDtoResponse.getBody().getComponent().getId(),
                String.valueOf(connectionDtoResponse.getBody().getRevision().getVersion()),
                processGroup,
                modelId
            );
            return entity;
        }
        throw new HttpClientErrorException(connectionDtoResponse.getStatusCode());
    }

    private Processor getSourceProcessorByModelId(Link link, Set<Processor> processors) {        
        UUID sourceUuid;
        if(link.getIn() != null) {
            sourceUuid = link.getIn().getId();
        } else {
            sourceUuid = link.getSource().getId();
        }
        for (Processor processor : processors) {
            if(processor.getModelId().equals(sourceUuid)) {
                return processor;
            }
        }
        return null;
    }

    private Processor getDestinationProcessorByModelId(Link link, Set<Processor> processors) {        
        UUID sinkUuid;
        if(link.getOut() != null) {
            sinkUuid = link.getOut().getId();
        } else {
            sinkUuid = link.getSink().getId();
        }
        for (Processor processor : processors) {
            if(processor.getModelId().equals(sinkUuid)) {
                return processor;
            }
        }
        return null;
    }

    private Processor createProcessor(ProcessGroup pg, ProcessorDTO processorDTO, UUID modelId) {
        ResponseEntity<ProcessorEntity> processorResponse;
        processorResponse = niFiService.createProcessor(pg.getId(), processorDTO);
        if(processorResponse.getStatusCode() == HttpStatus.CREATED) {
            Processor processorEntity = new Processor(
                processorResponse.getBody().getComponent().getId(),
                String.valueOf(processorResponse.getBody().getRevision().getVersion()),
                pg,
                modelId
            );
            return processorEntity;
        }
        throw new HttpClientErrorException(processorResponse.getStatusCode());
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
