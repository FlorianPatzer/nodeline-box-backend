package de.nodeline.box.application;

import de.nodeline.box.application.secondaryadapter.DeviceRepositoryInterface;
import de.nodeline.box.domain.model.Device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepositoryInterface deviceRepository;

    public List<Device> getAllDevices() {
        return new ArrayList<>(deviceRepository.findAll());
    }

    public Optional<Device> getDeviceById(UUID id) {
        return deviceRepository.findById(id);
    }

    public Device createDevice(Device device) {
        return deviceRepository.save(device);
    }

    public Optional<Device> updateDevice(UUID id, Device device) {
        if(deviceRepository.existsById(id)) {
            return Optional.of(deviceRepository.save(device));
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

