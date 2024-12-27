package de.nodeline.box.application.primaryadapter.api;

import de.nodeline.box.application.acl.api.DeviceService;
import de.nodeline.box.application.primaryadapter.api.dto.DeviceDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public ResponseEntity<List<DeviceDto>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable UUID id) {
        Optional<DeviceDto> device = deviceService.getDeviceById(id);
        return device.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DeviceDto> createDevice(@RequestBody DeviceDto device) {
        DeviceDto createdDevice = deviceService.createDevice(device);
        return ResponseEntity.status(201).body(createdDevice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceDto> updateDevice(@PathVariable UUID id, @RequestBody DeviceDto device) {
        Optional<DeviceDto> updatedDevice = deviceService.updateDevice(id, device);
        return updatedDevice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID id) {
        boolean deleted = deviceService.deleteDevice(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
