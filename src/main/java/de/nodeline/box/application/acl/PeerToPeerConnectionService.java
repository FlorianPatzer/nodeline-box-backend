package de.nodeline.box.application.acl;

import de.nodeline.box.application.primaryadapter.api.dto.LinkableDto;
import de.nodeline.box.application.primaryadapter.api.dto.LinkableReferenceDto;
import de.nodeline.box.application.primaryadapter.api.dto.PeerToPeerDto;
import de.nodeline.box.application.secondaryadapter.DataSinkRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.DataSourceRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.JoltTransformationRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.PeerToPeerConnectionRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.PipelineRepositoryInterface;
import de.nodeline.box.domain.model.DataSink;
import de.nodeline.box.domain.model.DataSource;
import de.nodeline.box.domain.model.JoltTransformation;
import de.nodeline.box.domain.model.PeerToPeerConnection;
import de.nodeline.box.domain.model.Pipeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PeerToPeerConnectionService {

    @Autowired
    private PeerToPeerConnectionRepositoryInterface ptpRepository;
    @Autowired
    private JoltTransformationRepositoryInterface joltTransRepository;
    @Autowired
    private DataSinkRepositoryInterface dataSinkRepository;
    @Autowired
    private DataSourceRepositoryInterface dataSourceRepository;
    @Autowired
    private PipelineRepositoryInterface pipelineRepository;

    public PeerToPeerConnection toEntity(PeerToPeerDto dto) {
        PeerToPeerConnection entity = new PeerToPeerConnection();
        entity.setId(dto.getId());
        if(dto.getSourceLinkableRef() != null) {
            switch (dto.getSourceLinkableRef().getType()) {
                case LinkableDto.Type.JOLT_TRANSFORMATION:
                    Optional<JoltTransformation> inEntity = joltTransRepository.findById(dto.getSourceLinkableRef().getId());
                    if(inEntity.isPresent()) {
                        entity.setIn(inEntity.get());
                    }
                    //break;
            }
        }
        if(dto.getTargetLinkableRef() != null) {
            switch (dto.getTargetLinkableRef().getType()) {
                case LinkableDto.Type.JOLT_TRANSFORMATION:
                    Optional<JoltTransformation> inEntity = joltTransRepository.findById(dto.getTargetLinkableRef().getId());
                    if(inEntity.isPresent()) {
                        entity.setOut(inEntity.get());
                    }
                    //break;
            }
        }
        if(dto.getSinkId() != null) {
            Optional<DataSink> sinkEntity = dataSinkRepository.findById(dto.getSinkId());
            if(sinkEntity.isPresent()) {
                entity.setSink(sinkEntity.get());
            }
        }
        if(dto.getSourceId() != null) {
            Optional<DataSource> sourceEntity = dataSourceRepository.findById(dto.getSourceId());
            if(sourceEntity.isPresent()) {
                entity.setSource(sourceEntity.get());
            }
        }
        if(dto.getPipelineId() != null) {
            Optional<Pipeline> pipEntity = pipelineRepository.findById(dto.getPipelineId());
            if(pipEntity.isPresent()) {
                entity.setPipeline(pipEntity.get());
            }
        }
        return entity;
    }

    public PeerToPeerDto toDto(PeerToPeerConnection entity) {
        PeerToPeerDto dto = new PeerToPeerDto();
        dto.setId(entity.getId());
        if(entity.getPipeline() != null) {
            dto.setPipelineId(entity.getPipeline().getId());
        }
        if(entity.getIn() != null) {
            LinkableReferenceDto linkableDto = new LinkableReferenceDto();
            linkableDto.setId(entity.getIn().getId());
            switch (entity.getIn()) {
                case JoltTransformation trans:
                    linkableDto.setType(LinkableDto.Type.JOLT_TRANSFORMATION);
                    dto.setSourceLinkableRef(linkableDto);
                    break;
            
                default:
                    break;
            }
        }
        if(entity.getOut() != null) {
            LinkableReferenceDto linkableDto = new LinkableReferenceDto();
            linkableDto.setId(entity.getOut().getId());
            switch (entity.getOut()) {
                case JoltTransformation trans:
                    linkableDto.setType(LinkableDto.Type.JOLT_TRANSFORMATION);
                    dto.setTargetLinkableRef(linkableDto);
                    break;
            
                default:
                    break;
            }
        }
        if(entity.getSource() != null) {
            dto.setSourceId(entity.getSource().getId());
        }
        if(entity.getSink() != null) {
            dto.setSinkId(entity.getSink().getId());
        }
        return dto;
    }

    public List<PeerToPeerDto> getAllLinks() {
        return ptpRepository.findAll().stream().map(ptp -> this.toDto((PeerToPeerConnection) ptp)).toList();
    }

    public Optional<PeerToPeerDto> getLinkById(UUID id) {
        return ptpRepository.findById(id).map(ptp -> this.toDto((PeerToPeerConnection) ptp));
    }

    public PeerToPeerDto createLink(PeerToPeerDto link) {
        return this.toDto(ptpRepository.save(this.toEntity(link)));
    }

    public Optional<PeerToPeerDto> updateLink(UUID id, PeerToPeerDto link) {
        if(ptpRepository.existsById(id)) {
            return Optional.of(ptpRepository.save(this.toEntity(link))).map(ptp -> this.toDto(ptp));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteLink(UUID id) {
        if(ptpRepository.existsById(id)) {
            ptpRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

