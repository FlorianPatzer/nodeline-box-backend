package de.nodeline.box.application.acl;

import de.nodeline.box.application.primaryadapter.api.dto.DataSinkDto;
import de.nodeline.box.application.primaryadapter.api.dto.DelivererDto;
import de.nodeline.box.application.primaryadapter.api.dto.LinkDto;
import de.nodeline.box.application.secondaryadapter.DataSinkRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.HttpPostRequestRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.PeerToPeerRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.PipelineRepositoryInterface;
import de.nodeline.box.domain.model.DataSink;
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
    private HttpPostRequestRepositoryInterface httpPostRequestRepository;
    @Autowired
    private PeerToPeerRepositoryInterface peerToPeerRepository;
    @Autowired
    private PipelineRepositoryInterface pipelineRepository;


    public DataSink toEntity(DataSinkDto dto) {
        DataSink entity = new DataSink();
        entity.setId(dto.getId());
        if(dto.getDeliverer() != null) {
            switch (dto.getDeliverer().getType()) {
                case DelivererDto.Type.POST_REQUEST:
                    Optional<HttpPostRequest> reqEntity = httpPostRequestRepository.findById(dto.getDeliverer().getId());
                    if(reqEntity.isPresent()) {
                        entity.setDeliverer(reqEntity.get());
                    }
                    break;
            
                default:
                    break;
            }
        }
        if(! dto.getInboundLinks().isEmpty()) {
            dto.getInboundLinks().forEach(link -> {
                switch (link.getType()) {
                    case LinkDto.Type.PEER_TO_PEER:
                        Optional<PeerToPeerConnection> conEntity = peerToPeerRepository.findById(link.getId());
                        if(conEntity.isPresent()) {
                            entity.addIn(conEntity.get());
                        }
                        break;
                
                    default:
                        break;
                }
            });
        }
        if(! dto.getPipelineIds().isEmpty()) {
            dto.getPipelineIds().forEach(pipelineId -> {
                Optional<Pipeline> pipEntity = pipelineRepository.findById(pipelineId);
                if(pipEntity.isPresent()) {
                    entity.addPipeline(pipEntity.get());
                }
            });
        }
        return entity;
    }

    public DataSinkDto toDto(DataSink entity) {
        DataSinkDto dto = new DataSinkDto();
        dto.setId(entity.getId());
        if(entity.getDeliverer() != null) {
            switch (entity.getDeliverer()) {
                case HttpPostRequest deliverer:
                    DelivererDto ddto = new DelivererDto();
                    ddto.setId(deliverer.getId());
                    ddto.setType(DelivererDto.Type.POST_REQUEST);
                    dto.setDeliverer(ddto);
                    break;
            
                default:
                    break;
            }
        }
        if(! entity.getIn().isEmpty()) {
            entity.getIn().forEach(in -> {
                LinkDto linkDto = new LinkDto();
                switch (in) {
                    case PeerToPeerConnection l:
                        linkDto.setType(LinkDto.Type.PEER_TO_PEER);
                        linkDto.setId(l.getId());
                        dto.addInboundLink(linkDto);
                        break;
                
                    default:
                        break;
                }
            });
        }
        if(! entity.getPipelines().isEmpty()) {
            entity.getPipelines().forEach(pipeline -> {
                dto.addPipelineId(pipeline.getId());
            });
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

