package de.nodeline.box.application.acl.api;

import de.nodeline.box.application.primaryadapter.api.dto.EndpointDto;
import de.nodeline.box.application.primaryadapter.api.exceptions.ResourceNotFoundException;
import de.nodeline.box.application.secondaryadapter.DeviceRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.EndpointRepositoryInterface;
import de.nodeline.box.domain.model.Device;
import de.nodeline.box.domain.model.Endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EndpointService {

    @Autowired
    private EndpointRepositoryInterface endpointRepository;
    @Autowired
    private DeviceRepositoryInterface deviceRepository;

    public Endpoint toEntity(EndpointDto dto) {
        Endpoint entity = new Endpoint();
        entity.setId(dto.getId());
        if(dto.getDeviceId() != null) {
            Optional<Device> deviceEntity = deviceRepository.findById(dto.getDeviceId());
            if(deviceEntity.isPresent()) {
                entity.setDevice(deviceEntity.get());
            }
        }
        return entity;
    }

    public EndpointDto toDto(Endpoint entity) {
        EndpointDto dto = new EndpointDto();
        dto.setId(entity.getId());
        if(entity.getDevice() != null) {
            dto.setDeviceId(entity.getDevice().getId());
        }
        return dto;
    }

    public List<EndpointDto> getAllEndpoints() {
        return endpointRepository.findAll().stream().map(endpoint -> this.toDto(endpoint)).toList();
    }

    public EndpointDto getEndpointById(UUID id) {
        Optional<Endpoint> endpoint = endpointRepository.findById(id);
        if(endpoint.isPresent()) {
            return this.toDto(endpoint.get());
        }
        throw new ResourceNotFoundException("Endpoint with ID " + id + " not found");
    }

    public EndpointDto createEndpoint(EndpointDto endpoint) {
        return this.toDto(endpointRepository.save(this.toEntity(endpoint)));
    }

    public EndpointDto updateEndpoint(UUID id, EndpointDto endpointDto) {
        if(endpointRepository.existsById(id)) {
            Endpoint endpoint = endpointRepository.save(this.toEntity(endpointDto));
            return this.toDto(endpoint);
        }
        throw new ResourceNotFoundException("Endpoint with ID " + id + " not found");
    }

    public boolean deleteEndpoint(UUID id) {
        if(endpointRepository.existsById(id)) {
            endpointRepository.deleteById(id);
            return true;
        }
        throw new ResourceNotFoundException("Endpoint with ID " + id + " not found");
    }
}

