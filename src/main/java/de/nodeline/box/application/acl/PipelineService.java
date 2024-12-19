package de.nodeline.box.application.acl;

import de.nodeline.box.application.primaryadapter.api.dto.PeerToPeerDto;
import de.nodeline.box.application.primaryadapter.api.dto.PipelineDto;
import de.nodeline.box.application.secondaryadapter.PipelineRepositoryInterface;
import de.nodeline.box.domain.model.PeerToPeerConnection;
import de.nodeline.box.domain.model.Pipeline;
import de.nodeline.box.domain.model.Transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Pipeline toEntity(PipelineDto dto) {
        Pipeline entity = new Pipeline();
        entity.setId(dto.getId());
        dto.getDataSinks().forEach(dsDto -> {
            entity.addDataSink(dataSinkService.toEntity(dsDto));
        });
        dto.getDataSources().forEach(dsDto -> {
            entity.addDataSource(dataSourceService.toEntity(dsDto));
        });
        dto.getLinkables().forEach(linkableDto -> {
            Transformation transEntity = transformationService.toEntity(linkableDto);
            entity.addLinkable(transEntity);
        });
        dto.getLinks().forEach(link -> {
            PeerToPeerConnection conEntity = peertoPeerConnectionService.toEntity(link);
            entity.addLink(conEntity);
        });
        return entity;
    }

    public PipelineDto toDto(Pipeline entity) {
        PipelineDto dto = new PipelineDto();
        dto.setId(entity.getId());
        entity.getDataSinks().forEach(ds -> {                
            dto.addDataSink(dataSinkService.toDto(ds));
        });
        entity.getDataSources().forEach(ds -> {
            dto.addDataSource(dataSourceService.toDto(ds));
        });
        entity.getLinks().forEach(link -> {
            PeerToPeerDto ptpDto = peertoPeerConnectionService.toDto((PeerToPeerConnection) link);
            dto.addLink(ptpDto);
        });
        entity.getLinkables().forEach(linkable -> {
            dto.addLinkable(transformationService.toDto((Transformation) linkable));
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
        return this.toDto(pipelineRepository.save(this.toEntity(pipeline)));
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
            pipelineRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

