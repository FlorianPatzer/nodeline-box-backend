package de.nodeline.box.application.acl.api;

import de.nodeline.box.application.primaryadapter.api.dto.EndpointDto;
import de.nodeline.box.application.primaryadapter.api.dto.RestEndpointAttributesDto;
import de.nodeline.box.application.primaryadapter.api.exceptions.ResourceNotFoundException;
import de.nodeline.box.application.secondaryadapter.DeviceRepositoryInterface;
import de.nodeline.box.application.secondaryadapter.RestEndpointRepositoryInterface;
import de.nodeline.box.domain.model.Device;
import de.nodeline.box.domain.model.Endpoint;
import de.nodeline.box.domain.model.RestEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RestEndpointService {

    @Autowired
    private RestEndpointRepositoryInterface endpointRepository;
    @Autowired
    private DeviceRepositoryInterface deviceRepository;
    private static final Logger logger = LoggerFactory.getLogger(RestEndpointService.class);

    public Endpoint toEntity(EndpointDto dto) {
        RestEndpoint entity = new RestEndpoint();
        if(dto.getId() != null) {
            entity.setId(dto.getId());
        }
        if(dto.getDeviceId() != null) {
            Optional<Device> deviceEntity = deviceRepository.findById(dto.getDeviceId());
            if(deviceEntity.isPresent()) {
                entity.setDevice(deviceEntity.get());
            } else {
                logger.error("Device with ID " + dto.getDeviceId() + " not found");
            }
        }
        entity.setName(dto.getName());
        RestEndpointAttributesDto attr = (RestEndpointAttributesDto) dto.getAttributes();
        entity.setBaseUrl(attr.getBaseUrl());
        return entity;
    }

    public EndpointDto toDto(Endpoint entity) {
        RestEndpoint restEndpoint = (RestEndpoint) entity;
        EndpointDto dto = new EndpointDto();
        dto.setId(restEndpoint.getId());
        if(restEndpoint.getDevice() != null) {
            dto.setDeviceId(restEndpoint.getDevice().getId());
        }
        RestEndpointAttributesDto attr = new RestEndpointAttributesDto();
        attr.setBaseUrl(restEndpoint.getBaseUrl());
        dto.setAttributes(attr);
        dto.setName(restEndpoint.getName());
        return dto;
    }

    public List<EndpointDto> getAllEndpoints() {
        return endpointRepository.findAll().stream().map(endpoint -> this.toDto(endpoint)).toList();
    }

    public EndpointDto getEndpointById(UUID id) {
        Optional<RestEndpoint> endpoint = endpointRepository.findById(id);
        if(endpoint.isPresent()) {
            return this.toDto(endpoint.get());
        }
        throw new ResourceNotFoundException("Endpoint with ID " + id + " not found");
    }

    public EndpointDto createEndpoint(EndpointDto endpoint) {
        return this.toDto(endpointRepository.save((RestEndpoint) this.toEntity(endpoint)));
    }

    public EndpointDto updateEndpoint(UUID id, EndpointDto endpointDto) {
        if(endpointRepository.existsById(id)) {
            RestEndpoint endpoint = endpointRepository.save((RestEndpoint) this.toEntity(endpointDto));
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

