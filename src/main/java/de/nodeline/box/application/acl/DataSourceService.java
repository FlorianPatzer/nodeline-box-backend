package de.nodeline.box.application.acl;

import de.nodeline.box.application.primaryadapter.api.dto.DataSourceDto;
import de.nodeline.box.application.primaryadapter.api.dto.PeerToPeerDto;
import de.nodeline.box.application.primaryadapter.api.dto.ProcurerDto;
import de.nodeline.box.application.secondaryadapter.DataSourceRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.HttpGetRequestRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.PeerToPeerRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.PipelineRepositoryInterface;
import de.nodeline.box.domain.model.DataSource;
import de.nodeline.box.domain.model.HttpGetRequest;
import de.nodeline.box.domain.model.PeerToPeerConnection;
import de.nodeline.box.domain.model.Pipeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataSourceService {

    @Autowired
    private DataSourceRepositoryInterface dataSourceRepository;
    @Autowired
    private HttpGetRequestRepositoryInterface httpGetRequestRepository;
    @Autowired
    private PeerToPeerRepositoryInterface peerToPeerRepository;
    @Autowired
    private PipelineRepositoryInterface pipelineRepository;


    public DataSource toEntity(DataSourceDto dto) {
        DataSource entity = new DataSource();
        entity.setId(dto.getId());
        if(dto.getProcurer() != null) {
            switch (dto.getProcurer().getType()) {
                case ProcurerDto.Type.GET_REQUEST:
                    Optional<HttpGetRequest> reqEntity = httpGetRequestRepository.findById(dto.getProcurer().getId());
                    if(reqEntity.isPresent()) {
                        entity.setProcurer(reqEntity.get());
                    }
                    break;
            
                default:
                    break;
            }
        }
        if(! dto.getOutboundLinks().isEmpty()) {
            dto.getOutboundLinks().forEach(link -> {
                Optional<PeerToPeerConnection> conEntity = peerToPeerRepository.findById(link.getId());
                if(conEntity.isPresent()) {
                    entity.addOut(conEntity.get());
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

    public DataSourceDto toDto(DataSource entity) {
        DataSourceDto dto = new DataSourceDto();
        dto.setId(entity.getId());
        if(entity.getProcurer() != null) {
            switch (entity.getProcurer()) {
                case HttpGetRequest procurer:
                    ProcurerDto ddto = new ProcurerDto();
                    ddto.setId(procurer.getId());
                    ddto.setType(ProcurerDto.Type.GET_REQUEST);
                    dto.setProcurer(ddto);
                    break;
            
                default:
                    break;
            }
        }
        if(! entity.getOut().isEmpty()) {
            entity.getOut().forEach(out -> {
                if(out instanceof PeerToPeerConnection) {
                    PeerToPeerDto linkDto = new PeerToPeerDto();
                    linkDto.setId(out.getId());
                    dto.addOutboundLink(linkDto);
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

    public Optional<DataSourceDto> updateDataSource(UUID id, DataSourceDto dataSourceDto) {
        DataSource dataSource = toEntity(dataSourceDto);
        if(dataSourceRepository.existsById(id)) {
            Optional<DataSourceDto> res = Optional.of(dataSourceRepository.save(dataSource)).map(ds -> this.toDto(ds));
            return res;
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteDataSource(UUID id) {
        if(dataSourceRepository.existsById(id)) {
            dataSourceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
