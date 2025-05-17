package de.nodeline.box.application.acl.api;

import de.nodeline.box.application.primaryadapter.api.dto.DeviceDto;
import de.nodeline.box.application.primaryadapter.api.exceptions.ResourceNotFoundException;
import de.nodeline.box.application.secondaryadapter.DeviceRepositoryInterface;
import de.nodeline.box.domain.model.Device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepositoryInterface deviceRepository;
    @Autowired
    private RestEndpointService endpointService;

    public Device toEntity(DeviceDto dto) {
        Device entity = new Device();
        if(dto.getId() != null) {
            entity.setId(dto.getId());
        }
        dto.getEndpoints().forEach(endpoint -> {
            entity.addEndpoint(endpointService.toEntity(endpoint));
        });
        if(dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if(dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        return entity;
    }

    public DeviceDto toDto(Device entity) {
        DeviceDto dto = new DeviceDto();
        dto.setId(entity.getId());
        entity.getEndpoints().forEach(endpoint -> {
            dto.addEndpoint(endpointService.toDto(endpoint));
        });
        if(entity.getName() != null) {
            dto.setName(entity.getName());
        }
        if(entity.getDescription() != null) {
            dto.setDescription(entity.getDescription());
        }
        return dto;
    }

    public List<DeviceDto> getAllDevices() {
        return deviceRepository.findAll().stream().map(device -> this.toDto(device)).toList();
    }

    public DeviceDto getDeviceById(UUID id) {
        Optional<Device> device = deviceRepository.findById(id);
        if(device.isPresent()) {
            return this.toDto(device.get());
        }
        throw new ResourceNotFoundException("Device with ID " + id + " not found");
    }

    public DeviceDto createDevice(DeviceDto device) {
        Device deviceEntity = this.toEntity(device);
        return this.toDto(deviceRepository.save(deviceEntity));
    }

    public DeviceDto updateDevice(UUID id, DeviceDto deviceDto) {
        if(deviceRepository.existsById(id)) {
            Device device = deviceRepository.save(this.toEntity(deviceDto));
            return this.toDto(device);
        }
        throw new ResourceNotFoundException("Device with ID " + id + " not found");
    }

    public void deleteDevice(UUID id) {
        if(deviceRepository.existsById(id)) {
            deviceRepository.deleteById(id);
            return;
        }
        throw new ResourceNotFoundException("Device with ID " + id + " not found");
    }
}

