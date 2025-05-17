package de.nodeline.box.application.acl.api;

import de.nodeline.box.application.primaryadapter.api.dto.JoltTransformationAttributesDto;
import de.nodeline.box.application.primaryadapter.api.dto.LinkableDto;
import de.nodeline.box.application.primaryadapter.api.exceptions.InvalidArgumentException;
import de.nodeline.box.application.primaryadapter.api.exceptions.ResourceNotFoundException;
import de.nodeline.box.application.secondaryadapter.JoltTransformationRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.PeerToPeerRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.PipelineRepositoryInterface;
import de.nodeline.box.domain.model.JoltTransformation;
import de.nodeline.box.domain.model.PeerToPeerConnection;
import de.nodeline.box.domain.model.Pipeline;
import de.nodeline.box.domain.model.Transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransformationService {

    @Autowired
    private JoltTransformationRepositoryInterface joltTransformationRepository;
    @Autowired
    private PipelineRepositoryInterface pipelineRepository;
    @Autowired
    private PeerToPeerRepositoryInterface peerToPeerRepository;

    public LinkableDto toDto(Transformation entity) {
        LinkableDto dto = new LinkableDto();
        switch (entity) {
            case JoltTransformation trans:
                dto.setType(LinkableDto.Type.JOLT_TRANSFORMATION);
                JoltTransformationAttributesDto jtaDto = new JoltTransformationAttributesDto();
                jtaDto.setSpec(trans.getJoltSpecification());
                dto.setAttributes(jtaDto);
                break;
        
            default:
                throw new InvalidArgumentException("Type of provided transformation entity not supported: " + entity.getClass().getName());
        }
        dto.setId(entity.getId());
        entity.getIn().forEach(link -> {
            switch (link) {
                case PeerToPeerConnection con:
                    dto.addInboundLinkId(con.getId());
                    break;
                default:
                    throw new InvalidArgumentException("Type of provided link entity not supported: " + link.getClass().getName());
            }
        });
        entity.getOut().forEach(link -> {
            switch (link) {
                case PeerToPeerConnection con:
                dto.addOutboundLinkId(link.getId());
                    break;
                default:
                    throw new InvalidArgumentException("Type of provided link entity not supported: " + link.getClass().getName());
            }
        });
        if(entity.getPipeline() != null) {
            dto.setPipelineId(entity.getPipeline().getId());
        }

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
                
        return dto;
    }

    public Transformation toEntity(LinkableDto dto) {
        Transformation entity;
        switch (dto.getType()) {
            case LinkableDto.Type.JOLT_TRANSFORMATION:                
                entity = new JoltTransformation();
                ((JoltTransformation) entity).setJoltSpecification(((JoltTransformationAttributesDto) dto.getAttributes()).getSpec());
                break;
        
            default:
                throw new InvalidArgumentException("Type of provided transformation entity not supported: " + dto.getType());
        }
        
        if(dto.getId() != null) {
            entity.setId(dto.getId());
        }
        if(dto.getPipelineId() != null) {
            Optional<Pipeline> pipEntity = pipelineRepository.findById(dto.getPipelineId());
            if(pipEntity.isPresent()) {
                entity.setPipeline(pipEntity.get());
            } else {  
                throw new ResourceNotFoundException("No pipeline found with id" + dto.getPipelineId());
            }
        } else {
            throw new InvalidArgumentException("Pipeline id required for transformation " + dto.getId());
        }
        dto.getInboundLinkIds().forEach(linkId -> {
            Optional<PeerToPeerConnection> conEntity = peerToPeerRepository.findById(linkId);
            if(conEntity.isPresent()) {
                entity.addIn(conEntity.get());
            }
        });
        dto.getOutboundLinkIds().forEach(linkId -> {
            Optional<PeerToPeerConnection> conEntity = peerToPeerRepository.findById(linkId);
            if(conEntity.isPresent()) {
                entity.addOut(conEntity.get());
            }
        });

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public List<LinkableDto> getAllTransformations() {
        List<LinkableDto> linkables = joltTransformationRepository.findAll().stream().map(trans -> this.toDto(trans)).toList();
        // retrieve other transformations here
        return linkables;
    }

    public Optional<LinkableDto> getTransformationById(UUID id) {
        Optional<LinkableDto> linkable = joltTransformationRepository.findById(id).map(trans -> this.toDto(trans));
        // retrieve other transformations here
        return linkable;
    }

    public LinkableDto createTransformation(LinkableDto transformationDto) {
        LinkableDto resultDto;
        Transformation transformation = this.toEntity(transformationDto);
        switch (transformation) {
            case JoltTransformation trans:
                resultDto = this.toDto(joltTransformationRepository.save(trans));
                break;
        // handle other transformations here
            default:
                throw new InvalidArgumentException("Type of provided transformation entity not supported: " + transformation.getClass().getName());
        }
        return resultDto;
    }

    public LinkableDto updateTransformation(UUID id, LinkableDto transformationDto) {
        Transformation transformation = this.toEntity(transformationDto);
        switch (transformation) {
            case JoltTransformation trans:
                if(joltTransformationRepository.existsById(id)) {
                    return toDto(joltTransformationRepository.save(trans));
                } else {
                    throw new ResourceNotFoundException("No transformation found with id " + id);
                }
        // handle other transformations here
            default:
                throw new InvalidArgumentException("Type of provided transformation entity not supported: " + transformation.getClass().getName());
        }
        
    }

    public void deleteTransformation(UUID id) {
        if(joltTransformationRepository.existsById(id)) {
            joltTransformationRepository.deleteById(id);
            return;
        }
        throw new ResourceNotFoundException("No transformation found with id " + id);
    }
}

