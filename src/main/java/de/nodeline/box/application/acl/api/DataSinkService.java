package de.nodeline.box.application.acl.api;

import de.nodeline.box.application.primaryadapter.api.dto.DataSinkDto;
import de.nodeline.box.application.primaryadapter.api.dto.DelivererDto;
import de.nodeline.box.application.primaryadapter.api.dto.HttpPostRequestAttributesDto;
import de.nodeline.box.application.secondaryadapter.DataSinkRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.EndpointRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.PeerToPeerRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.PipelineRepositoryInterface;
import de.nodeline.box.domain.model.DataSink;
import de.nodeline.box.domain.model.Endpoint;
import de.nodeline.box.domain.model.HttpPostRequest;
import de.nodeline.box.domain.model.PeerToPeerConnection;
import de.nodeline.box.domain.model.Pipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataSinkService {

    @Autowired
    private DataSinkRepositoryInterface dataSinkRepository;
    @Autowired
    private PeerToPeerRepositoryInterface peerToPeerRepository;
    @Autowired
    private PipelineRepositoryInterface pipelineRepository;
    @Autowired
    private EndpointRepositoryInterface endpointRepository;



    public DataSink toEntity(DataSinkDto dto) {
        DataSink entity = new DataSink();
        entity.setId(dto.getId());
        if(dto.getDeliverer() != null) {
            switch (dto.getDeliverer().getType()) {
                case DelivererDto.Type.POST_REQUEST:
                    HttpPostRequest reqEntity = new HttpPostRequest();
                    reqEntity.setId(dto.getDeliverer().getId());
                    reqEntity.setUrl(((HttpPostRequestAttributesDto) dto.getDeliverer().getAttributes()).getUrl());
                    if(((HttpPostRequestAttributesDto) dto.getDeliverer().getAttributes()).getEndpointId() != null) {
                        Optional<Endpoint> endpointEntity = endpointRepository.findById(((HttpPostRequestAttributesDto) dto.getDeliverer().getAttributes()).getEndpointId());
                        if(endpointEntity.isPresent()) {
                            reqEntity.setEndpoint(endpointEntity.get());
                        }
                    }
                    entity.setDeliverer(reqEntity);
                    break;
            
                default:
                    break;
            }
        }
        if(! dto.getInboundLinkIds().isEmpty()) {
            dto.getInboundLinkIds().forEach(linkId -> {
                Optional<PeerToPeerConnection> conEntity = peerToPeerRepository.findById(linkId);
                if(conEntity.isPresent()) {
                    entity.addIn(conEntity.get());
                }
            });
        }
        if(dto.getPipelineId() != null) {
            Optional<Pipeline> pipEntity = pipelineRepository.findById(dto.getPipelineId());
            if(pipEntity.isPresent()) {
                entity.setPipeline(pipEntity.get());
            } else {                
                throw new IllegalArgumentException("No pipeline found with id" + dto.getPipelineId());
            }
        } else {
            throw new IllegalArgumentException("Pipeline id required for data sink " + dto.getId());
        }
        return entity;
    }

    public DataSinkDto toDto(DataSink entity) {
        DataSinkDto dto = new DataSinkDto();
        dto.setId(entity.getId());
        if(entity.getDeliverer() != null) {
            switch (entity.getDeliverer()) {
                case HttpPostRequest deliverer:
                    DelivererDto delivererDto = new DelivererDto();
                    delivererDto.setId(deliverer.getId());
                    delivererDto.setType(DelivererDto.Type.POST_REQUEST);
                    HttpPostRequestAttributesDto delivererAttrDto =  new HttpPostRequestAttributesDto();
                    if(deliverer.getEndpoint() != null) {
                        delivererAttrDto.setEndpointId(deliverer.getEndpoint().getId());
                    }
                    delivererAttrDto.setUrl(deliverer.getUrl());
                    delivererDto.setAttributes(delivererAttrDto);
                    dto.setDeliverer(delivererDto);
                    break;
            
                default:
                    break;
            }
        }
        if(! entity.getIn().isEmpty()) {
            entity.getIn().forEach(in -> {
                if(in instanceof PeerToPeerConnection) {
                    dto.addInboundLinkId(in.getId());
                }
            });
        }
        if(entity.getPipeline() != null) {
            dto.setPipelineId(entity.getPipeline().getId());
        }
        return dto;
    }

    public List<DataSinkDto> getAllDataSinks() {
        return dataSinkRepository.findAll().stream().map(ds -> this.toDto(ds)).toList();
    }

    public Optional<DataSinkDto> getDataSinkById(UUID id) {
        return dataSinkRepository.findById(id).map(ds -> this.toDto(ds));
    }

    public DataSinkDto createDataSink(DataSinkDto dataSinkDto) {
        DataSink dataSink = toEntity(dataSinkDto);
        return this.toDto(dataSinkRepository.save(dataSink));
    }

    public Optional<DataSinkDto> updateDataSink(UUID id, DataSinkDto dataSinkDto) {
        DataSink dataSink = toEntity(dataSinkDto);
        if(dataSinkRepository.existsById(id)) {
            return Optional.of(dataSinkRepository.save(dataSink)).map(ds -> this.toDto(ds));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteDataSink(UUID id) {
        if(dataSinkRepository.existsById(id)) {
            dataSinkRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

