package de.nodeline.box.application.primaryadapter.api;

import de.nodeline.box.application.acl.api.DeviceService;
import de.nodeline.box.application.primaryadapter.api.dto.DeviceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Operation(summary = "Get all device types")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of device types")
    })
    @GetMapping
    public ResponseEntity<List<DeviceDto>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @Operation(summary = "Get all device types")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of device types")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable UUID id) {
        DeviceDto device = deviceService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    @Operation(summary = "Create a new device")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Device created")
    })
    @PostMapping
    public ResponseEntity<DeviceDto> createDevice(@RequestBody DeviceDto device) {
        DeviceDto createdDevice = deviceService.createDevice(device);
        return ResponseEntity.status(201).body(createdDevice);
    }

    @Operation(summary = "Update a device")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Device updated"),
        @ApiResponse(responseCode = "404", description = "Device not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DeviceDto> updateDevice(@PathVariable UUID id, @RequestBody DeviceDto device) {
        DeviceDto updatedDevice = deviceService.updateDevice(id, device);
        return ResponseEntity.ok(updatedDevice);
    }

    @Operation(summary = "Delete a device")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Device deleted"),
        @ApiResponse(responseCode = "404", description = "Device not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.notFound().build();
    }
}
