package de.nodeline.box.application.acl.nifi;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import de.nodeline.box.application.secondaryadapter.nifi.NiFiService;
import de.nodeline.box.application.secondaryadapter.nifi.NifiTransformationService;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ConnectionDTO;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ConnectionEntity;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ProcessGroupDTO;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ProcessGroupEntity;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ProcessorDTO;
import de.nodeline.box.application.secondaryadapter.nifi.dto.ProcessorEntity;
import de.nodeline.box.application.secondaryadapter.nifi.model.RelationshipInterface;
import de.nodeline.box.application.secondaryadapter.nifi.model.Connection;
import de.nodeline.box.application.secondaryadapter.nifi.model.ProcessGroup;
import de.nodeline.box.application.secondaryadapter.nifi.model.Processor;
import de.nodeline.box.application.secondaryadapter.nifi.model.Processor.Type;
import de.nodeline.box.application.acl.api.RestEndpointService;
import de.nodeline.box.application.secondaryadapter.NifiProcessGroupRepositoryInterface;
import de.nodeline.box.domain.model.EngineFlowStatus;
import de.nodeline.box.domain.model.Link;
import de.nodeline.box.domain.model.Pipeline;
import de.nodeline.box.domain.model.PipelineStatus;
import de.nodeline.box.domain.port.WorkflowEngine.EngineResponse;
import de.nodeline.box.domain.port.WorkflowEngine.WorkflowEngineService;

@Service
public class NifiWorkflowEngineService implements WorkflowEngineService {
    @Autowired
    private NiFiService niFiService;
    @Autowired
    private NifiProcessGroupRepositoryInterface pgRepo;
    @Autowired
    private NifiTransformationService transformationService;
    private static final Logger logger = LoggerFactory.getLogger(RestEndpointService.class);

    @Override
    public EngineResponse createFlow(Pipeline pipeline) {        
        ProcessGroupDTO pgDto = new ProcessGroupDTO();
        pgDto.setId(null);
        pgDto.setName(pipeline.getId().toString());
        pgDto.setComments("Process Group for nodeline box pipeline " + pipeline.getId().toString());
        pgDto.setPosition(null);
        ResponseEntity<ProcessGroupEntity> response = niFiService.createProcessGroup(pgDto);
        if(response.getStatusCode() == HttpStatus.CREATED) {
            String nifiProcessGroupId = response.getBody().getComponent().getId();
            String nifiProcessGroupVersion = String.valueOf(response.getBody().getRevision().getVersion());
            ProcessGroup pgEntity = new ProcessGroup(nifiProcessGroupId, pipeline.getId(), nifiProcessGroupVersion, EngineFlowStatus.STOPPED, new HashSet<>(), new HashSet<>());
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
                Processor sourceProcessor = getSourceProcessorByModelId(link,  pgEntity.getProcessors());
                Processor destinationProcessor = getDestinationProcessorByModelId(link,  pgEntity.getProcessors());

                //Select relationships for the connection
                HashSet<RelationshipInterface> relationships = new HashSet<>();
                relationships.addAll(getRelationshipsToSelectByDefault(sourceProcessor.getType()));

                ConnectionDTO connectionDTO = transformationService.linkToConnectionDTO(
                    link,
                    sourceProcessor.getId(),
                    destinationProcessor.getId(),
                    nifiProcessGroupId,
                    relationships
                );
                pgEntity.addConnection(createConnection(pgEntity, connectionDTO, link.getId()));
            });
            pgRepo.save(pgEntity);
            return new EngineResponse(EngineFlowStatus.STOPPED);
        }
        return new EngineResponse(EngineFlowStatus.ISSUE_EXISTS, response.getBody().toString());
    }

    private Collection<? extends RelationshipInterface> getRelationshipsToSelectByDefault(Type type) {
        HashSet<RelationshipInterface> relationships = new HashSet<>();
        switch (type) {
            case Processor.Type.HTTP_REQUEST:
                relationships.add(Processor.HttpRequestRelationship.RESPONSE);
                relationships.add(Processor.HttpRequestRelationship.ORIGINAL);
                return relationships;
            case Processor.Type.JOLT_TRANSFORMATION:
                relationships.add(Processor.JoltTransformationRelationship.SUCCESS);
                return relationships;
            default:
                throw new UnsupportedOperationException("Unsupported processor type found!");
        }
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
                modelId,
                processorDTO.getType()
            );
            return processorEntity;
        }
        throw new HttpClientErrorException(processorResponse.getStatusCode());
    }

    @Override
    public EngineResponse updateFlow(Pipeline pipeline) {
        EngineResponse response = deleteFlow(pipeline.getId());
        switch(response.getStatus()) {
            case DELETED:
                // Everything is fine, proceed with creating the new flow
                break;
            case NOT_FOUND:
                // The flow was not found, thus it can be created from scratch
                break;
            case ISSUE_EXISTS:
            default:
                // There was an issue deleting the flow, return the response
                return response;
        }    
        return createFlow(pipeline);
    }

    @Override
    public EngineResponse deleteFlow(UUID pipelineId) {
        ProcessGroup pgEntity = pgRepo.findByPipelineId(pipelineId);
        if(pgEntity == null) {
            logger.warn("No Process Group found for " + pipelineId);
            return new EngineResponse(EngineFlowStatus.NOT_FOUND);
        }
        ResponseEntity<String> response = niFiService.deleteProcessGroup(pgEntity.getId());
        switch(response.getStatusCode()) {
            case HttpStatus.NOT_FOUND:
                System.out.println("Process Group " + pgEntity.getId() + " not found");
                pgRepo.delete(pgEntity);
                return new EngineResponse(EngineFlowStatus.NOT_FOUND);
            case HttpStatus.OK:
                pgRepo.delete(pgEntity);
                return new EngineResponse(EngineFlowStatus.DELETED);
            default:
                pgEntity.setDeploymentStatus(EngineFlowStatus.ISSUE_EXISTS);
                pgRepo.save(pgEntity);
                return new EngineResponse(EngineFlowStatus.ISSUE_EXISTS, response.getBody());
        }
    }

    public NiFiService getNiFiService( ) {
        return niFiService;
    }

    public EngineResponse updateFlowStatus(Pipeline pipeline, PipelineStatus pipelineStatus) {
        ProcessGroup pgEntity = pgRepo.findByPipelineId(pipeline.getId());
        if(pgEntity == null) {
            logger.warn("No Process Group found for pipeline " + pipeline.getId() + ".");
            if(pipelineStatus == PipelineStatus.RUNNING) {
                logger.info("Trying to create a new Process Group for pipeline " + pipeline.getId() + ".");
                EngineResponse creationResponse = createFlow(pipeline);
                switch (creationResponse.getStatus()) {
                    case ISSUE_EXISTS:
                        creationResponse.setIssueMessage("No Process Group found for pipeline and issue arised when trying to create a new one: " 
                            + creationResponse.getIssueMessage());
                        return creationResponse;
                    case STOPPED:
                        return activateFlow(pgRepo.findByPipelineId(pipeline.getId()));
                    case RUNNING: // Nifi does not automatically start its newly created flows, thus there must be an issue
                    default: // DELETED, NOT_FOUND
                        return new EngineResponse(EngineFlowStatus.ISSUE_EXISTS, "Received unexpected EngineResponseStatus " + creationResponse.getStatus() + " when trying to create a new Process Group for pipeline " + pipeline.getId() + ".");
                }
            }        
            // Nothing to do for now, we may have to do something when the frow should be started
            return new EngineResponse(EngineFlowStatus.NOT_FOUND);
        } else {
            switch (pipelineStatus) {
                case RUNNING:
                    switch (pgEntity.getDeploymentStatus()) {
                        case RUNNING: // If we have a status "RUNNING" maybe the flow was stopped outside the controller, so let's make sure it is running
                        case STOPPED:
                            return activateFlow(pgEntity);
                        default:
                            return new EngineResponse(EngineFlowStatus.ISSUE_EXISTS, "Process Group has an issue and cannot be started.");
                    }
                case STOPPED:
                    switch (pgEntity.getDeploymentStatus()) {
                        case STOPPED: // If we have a status "STOPPED" maybe the flow was started outside the controller, so let's make sure it is stopped
                        case RUNNING:
                            return deactivateFlow(pgEntity);
                        default:
                            return new EngineResponse(EngineFlowStatus.ISSUE_EXISTS, "Process Group has an issue and cannot be stopped.");
                    }
                default:
                    throw new UnsupportedOperationException("Currently only pipeline status RUNNING and STOPPED are supported.");
            }            
        }
    }

    EngineResponse activateFlow(ProcessGroup processGroup) {
        ResponseEntity<String> response = niFiService.activateProcessGroup(processGroup.getId());
        if(response.getStatusCode() == HttpStatus.OK) {
            processGroup.setDeploymentStatus(EngineFlowStatus.RUNNING);
            pgRepo.save(processGroup);
            return new EngineResponse(EngineFlowStatus.RUNNING);
        }
        processGroup.setDeploymentStatus(EngineFlowStatus.ISSUE_EXISTS);
        pgRepo.save(processGroup);
        return new EngineResponse(EngineFlowStatus.ISSUE_EXISTS, response.getBody());
    }

    EngineResponse deactivateFlow(ProcessGroup processGroup) {
        ResponseEntity<String> response = niFiService.deactivateProcessGroup(processGroup.getId());
        if(response.getStatusCode() == HttpStatus.OK) {
            processGroup.setDeploymentStatus(EngineFlowStatus.STOPPED);
            pgRepo.save(processGroup);
            return new EngineResponse(EngineFlowStatus.STOPPED);
        }
        processGroup.setDeploymentStatus(EngineFlowStatus.ISSUE_EXISTS);
        pgRepo.save(processGroup);
        return new EngineResponse(EngineFlowStatus.ISSUE_EXISTS, response.getBody());
    }
}
