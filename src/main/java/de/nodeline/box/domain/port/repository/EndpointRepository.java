package de.nodeline.box.domain.port.repository;
/**
 * The Endpoint entity is shared between DataSource, DataSink, and Device. This repository handles endpoint persistence.
 */
import de.nodeline.box.domain.model.Endpoint;
import java.util.List;
import java.util.Optional;

public interface EndpointRepository {

    // Save an Endpoint
    void save(Endpoint endpoint);

    // Retrieve an Endpoint by address
    Optional<Endpoint> findByAddress(String address);

    // Retrieve all Endpoints
    List<Endpoint> findAll();

    // Delete an Endpoint by address
    void deleteByAddress(String address);
}
