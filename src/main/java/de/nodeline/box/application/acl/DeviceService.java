package de.nodeline.box.application.acl;

import de.nodeline.box.application.primaryadapter.api.dto.DeviceDto;
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
    private EndpointService endpointService;

    public Device toEntity(DeviceDto dto) {
        Device entity = new Device();
        entity.setId(dto.getId());
        dto.getEndpoints().forEach(endpoint -> {
            entity.addEndpoint(endpointService.toEntity(endpoint));
        });
        return entity;
    }

    public DeviceDto toDto(Device entity) {
        DeviceDto dto = new DeviceDto();
        dto.setId(entity.getId());
        entity.getEndpoints().forEach(endpoint -> {
            dto.addEndpoint(endpointService.toDto(endpoint));
        });
        return dto;
    }

    public List<DeviceDto> getAllDevices() {
        return deviceRepository.findAll().stream().map(device -> this.toDto(device)).toList();
    }

    public Optional<DeviceDto> getDeviceById(UUID id) {
        return deviceRepository.findById(id).map(device -> this.toDto(device));
    }

    public DeviceDto createDevice(DeviceDto device) {
        Device deviceEntity = this.toEntity(device);
        return this.toDto(deviceRepository.save(deviceEntity));
    }

    public Optional<DeviceDto> updateDevice(UUID id, DeviceDto device) {
        if(deviceRepository.existsById(id)) {
            return Optional.of(deviceRepository.save(this.toEntity(device))).map(dev -> this.toDto(dev));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteDevice(UUID id) {
        if(deviceRepository.existsById(id)) {
            deviceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

