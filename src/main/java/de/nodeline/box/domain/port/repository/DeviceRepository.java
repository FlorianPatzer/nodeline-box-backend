package de.nodeline.box.domain.port.repository;

/**
 * The Device entity contains Endpoint objects. The repository allows CRUD operations on Device.
 */
import de.nodeline.box.domain.model.Device;
import java.util.List;
import java.util.Optional;

public interface DeviceRepository {

    // Save a Device
    void save(Device device);

    // Retrieve a Device by ID
    Optional<Device> findById(String id);

    // Retrieve all Devices
    List<Device> findAll();

    // Delete a Device by ID
    void deleteById(String id);
}
