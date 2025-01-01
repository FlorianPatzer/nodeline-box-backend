package de.nodeline.box.application.acl.api;

import de.nodeline.box.application.acl.nifi.NifiWorkflowEngineService;
import de.nodeline.box.application.primaryadapter.api.dto.DataSinkDto;
import de.nodeline.box.application.primaryadapter.api.dto.DataSourceDto;
import de.nodeline.box.application.primaryadapter.api.dto.LinkableDto;
import de.nodeline.box.application.primaryadapter.api.dto.PeerToPeerDto;
import de.nodeline.box.application.primaryadapter.api.dto.PipelineDto;
import de.nodeline.box.application.secondaryadapter.PipelineRepositoryInterface;
import de.nodeline.box.domain.model.DataSink;
import de.nodeline.box.domain.model.DataSource;
import de.nodeline.box.domain.model.Linkable;
import de.nodeline.box.domain.model.PeerToPeerConnection;
import de.nodeline.box.domain.model.Pipeline;
import de.nodeline.box.domain.model.Transformation;

import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

import java.util.*;

@Service
public class PipelineService {

    @Autowired
    private PipelineRepositoryInterface pipelineRepository;
    @Autowired
    private TransformationService transformationService;
    @Autowired
    private PeerToPeerConnectionService peertoPeerConnectionService;
    @Autowired
    private DataSinkService dataSinkService;
    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private NifiWorkflowEngineService workflowEngineService;

    public Pipeline toEntity(PipelineDto dto) {
        Pipeline entity = new Pipeline();
        entity.setId(dto.getId());
        dto.getDataSinks().forEach(dsDto -> {
            DataSink ds = dataSinkService.toEntity(dsDto);
            ds.setPipeline(entity);
            entity.addDataSink(ds);
        });
        dto.getDataSources().forEach(dsDto -> {
            DataSource ds = dataSourceService.toEntity(dsDto);
            ds.setPipeline(entity);
            entity.addDataSource(ds);
        });
        dto.getLinkables().forEach(linkableDto -> {
            Transformation transEntity = transformationService.toEntity(linkableDto);
            transEntity.setPipeline(entity);
            entity.addLinkable(transEntity);
        });
        dto.getLinks().forEach(link -> {            
            PeerToPeerConnection conEntity = new PeerToPeerConnection();
            conEntity.setId(link.getId());
            if(link.getSourceLinkableRef() != null) {
                Linkable linkable = entity.getLinkables().stream().filter(l -> l.getId().equals(link.getSourceLinkableRef().getId())).findFirst().orElse(null);
                conEntity.setIn(linkable);    
            }
            if(link.getTargetLinkableRef() != null) {
                Linkable linkable = entity.getLinkables().stream().filter(l -> l.getId().equals(link.getTargetLinkableRef().getId())).findFirst().orElse(null);
                conEntity.setOut(linkable);    
            }
            if(link.getSinkId() != null) {
                DataSink sink = entity.getDataSinks().stream().filter(s -> s.getId().equals(link.getSinkId())).findFirst().orElse(null);
                conEntity.setSink(sink);
            }
            if(link.getSourceId() != null) {
                DataSource source = entity.getDataSources().stream().filter(s -> s.getId().equals(link.getSourceId())).findFirst().orElse(null);
                conEntity.setSource(source);
            }
            conEntity.setPipeline(entity);
            entity.addLink(conEntity);
        });
        return entity;
    }

    public PipelineDto toDto(Pipeline entity) {
        PipelineDto dto = new PipelineDto();
        dto.setId(entity.getId());
        entity.getDataSinks().forEach(ds -> {
            DataSinkDto dsDto = dataSinkService.toDto(ds);
            dsDto.setPipelineId(entity.getId());
            dto.addDataSink(dsDto);
        });
        entity.getDataSources().forEach(ds -> {
            DataSourceDto dsDto = dataSourceService.toDto(ds);
            dsDto.setPipelineId(entity.getId());
            dto.addDataSource(dataSourceService.toDto(ds));
        });
        entity.getLinks().forEach(link -> {
            PeerToPeerDto ptpDto = peertoPeerConnectionService.toDto((PeerToPeerConnection) link);
            ptpDto.setPipelineId(entity.getId());
            dto.addLink(ptpDto);
        });
        entity.getLinkables().forEach(linkable -> {
            LinkableDto transDto = transformationService.toDto((Transformation) linkable);
            transDto.setPipelineId(entity.getId());
            dto.addLinkable(transDto);
        });
        return dto;
    }

    public List<PipelineDto> getAllPipelines() {
        return pipelineRepository.findAll().stream().map(pip -> this.toDto(pip)).toList();
    }

    public Optional<PipelineDto> getPipelineById(UUID id) {
        return pipelineRepository.findById(id).map(pip -> this.toDto(pip));
    }

    public PipelineDto createPipeline(PipelineDto pipeline) {
        Pipeline pipelineEntity = this.toEntity(pipeline);
        if(!workflowEngineService.createPipeline(pipelineEntity)) {
            throw new RuntimeException("Unable to persist representative for pipeline " + pipeline.getId().toString());
        }
        return this.toDto(pipelineRepository.save(pipelineEntity));
    }

    public Optional<PipelineDto> updatePipeline(UUID id, PipelineDto pipeline) {
        if(pipelineRepository.existsById(id)) {
            return Optional.of(pipelineRepository.save(this.toEntity(pipeline))).map(pip -> this.toDto(pip));
        } else {
            return Optional.empty();
        }
    }

    public boolean deletePipeline(UUID id) {
        if(pipelineRepository.existsById(id)) {
            // TODO: Probably not the most performant way to do this:
            if(!workflowEngineService.deletePipeline(id)) {
                throw new RuntimeException("Unable to delete representative for pipeline " + id);
            }
            pipelineRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

