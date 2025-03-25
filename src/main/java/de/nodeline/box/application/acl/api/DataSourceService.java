package de.nodeline.box.application.acl.api;

import de.nodeline.box.application.primaryadapter.api.dto.DataSourceDto;
import de.nodeline.box.application.primaryadapter.api.dto.HttpGetRequestAttributesDto;
import de.nodeline.box.application.primaryadapter.api.dto.ProcurerDto;
import de.nodeline.box.application.primaryadapter.api.exceptions.InvalidArgumentException;
import de.nodeline.box.application.primaryadapter.api.exceptions.ResourceNotFoundException;
import de.nodeline.box.application.secondaryadapter.DataSourceRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.RestEndpointRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.PeerToPeerRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.PipelineRepositoryInterface;
import de.nodeline.box.domain.model.DataSource;
import de.nodeline.box.domain.model.HttpGetRequest;
import de.nodeline.box.domain.model.PeerToPeerConnection;
import de.nodeline.box.domain.model.Pipeline;
import de.nodeline.box.domain.model.RestEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataSourceService {

    @Autowired
    private DataSourceRepositoryInterface dataSourceRepository;
    @Autowired
    private PeerToPeerRepositoryInterface peerToPeerRepository;
    @Autowired
    private PipelineRepositoryInterface pipelineRepository;
    @Autowired
    private RestEndpointRepositoryInterface endpointRepository;


    public DataSource toEntity(DataSourceDto dto) {
        DataSource entity = new DataSource();
        entity.setId(dto.getId());
        if(dto.getProcurer() != null) {
            switch (dto.getProcurer().getType()) {
                case ProcurerDto.Type.GET_REQUEST:
                    HttpGetRequest reqEntity = new HttpGetRequest();
                    reqEntity.setId(dto.getProcurer().getId());
                    reqEntity.setRelativePath(((HttpGetRequestAttributesDto) dto.getProcurer().getAttributes()).getRelativePath());
                    if(((HttpGetRequestAttributesDto) dto.getProcurer().getAttributes()).getEndpointId() != null) {
                        Optional<RestEndpoint> endpointEntity = endpointRepository.findById(((HttpGetRequestAttributesDto) dto.getProcurer().getAttributes()).getEndpointId());
                        if(endpointEntity.isPresent()) {
                            reqEntity.setEndpoint(endpointEntity.get());
                        }
                    }
                    entity.setProcurer(reqEntity);
                    break;
            
                default:
                    break;
            }
        }
        if(! dto.getOutboundLinkIds().isEmpty()) {
            dto.getOutboundLinkIds().forEach(linkId -> {
                Optional<PeerToPeerConnection> conEntity = peerToPeerRepository.findById(linkId);
                if(conEntity.isPresent()) {
                    entity.addOut(conEntity.get());
                }
            });
        }
        if(dto.getPipelineId() != null) {
            Optional<Pipeline> pipEntity = pipelineRepository.findById(dto.getPipelineId());
            if(pipEntity.isPresent()) {
                entity.setPipeline(pipEntity.get());
            } else {                
                throw new ResourceNotFoundException("No pipeline found with id" + dto.getPipelineId());
            }
        } else {
            throw new InvalidArgumentException("Pipeline id required for data source " + dto.getId());
        }
        return entity;
    }

    public DataSourceDto toDto(DataSource entity) {
        DataSourceDto dto = new DataSourceDto();
        dto.setId(entity.getId());
        if(entity.getProcurer() != null) {
            switch (entity.getProcurer()) {
                case HttpGetRequest procurer:
                    ProcurerDto procDto = new ProcurerDto();
                    procDto.setId(procurer.getId());
                    procDto.setType(ProcurerDto.Type.GET_REQUEST);
                    HttpGetRequestAttributesDto procAttrDto =  new HttpGetRequestAttributesDto();
                    if(procurer.getEndpoint() != null) {
                        procAttrDto.setEndpointId(procurer.getEndpoint().getId());
                    }
                    procAttrDto.setRelativePath(procurer.getRelativePath());
                    procDto.setAttributes(procAttrDto);
                    dto.setProcurer(procDto);
                    break;
            
                default:
                    break;
            }
        }
        if(! entity.getOut().isEmpty()) {
            entity.getOut().forEach(out -> {
                if(out instanceof PeerToPeerConnection) {
                    dto.addOutboundLinkId(out.getId());
                }
            });
        }
        if(entity.getPipeline() != null) {
            dto.setPipelineId(entity.getPipeline().getId());
        }
        return dto;
    }

    public List<DataSourceDto> getAllDataSources() {
        return dataSourceRepository.findAll().stream().map(ds -> this.toDto(ds)).toList();
    }

    public Optional<DataSourceDto> getDataSourceById(UUID id) {
        return dataSourceRepository.findById(id).map(ds -> this.toDto(ds));
    }

    public DataSourceDto createDataSource(DataSourceDto dataSourceDto) {
        DataSource dataSource = toEntity(dataSourceDto);
        return this.toDto(dataSourceRepository.save(dataSource));
    }

    public DataSourceDto updateDataSource(UUID id, DataSourceDto dataSourceDto) {
        DataSource dataSource = toEntity(dataSourceDto);
        if(dataSourceRepository.existsById(id)) {
            return this.toDto(dataSourceRepository.save(dataSource));
        }
        throw new ResourceNotFoundException("No data source found with id " + id);
    }

    public void deleteDataSource(UUID id) {
        if(dataSourceRepository.existsById(id)) {
            dataSourceRepository.deleteById(id);
            return;
        }
        throw new ResourceNotFoundException("No data source found with id " + id);
    }
}

