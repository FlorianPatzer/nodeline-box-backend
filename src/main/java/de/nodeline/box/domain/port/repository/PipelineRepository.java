package de.nodeline.box.domain.port.repository;

/**
 * A Pipeline is associated with a NodeLineBox. The repository focuses on managing pipelines.
 */
import java.util.List;
import java.util.Optional;

import de.nodeline.box.domain.model.Pipeline;

public interface PipelineRepository {

    // Save a Pipeline
    void save(Pipeline pipeline);

    // Retrieve a Pipeline by ID
    Optional<Pipeline> findById(String id);

    // Retrieve all Pipelines for a given NodeLineBox ID
    List<Pipeline> findByNodeLineBoxId(String nodeLineBoxId);

    // Retrieve all Pipelines
    List<Pipeline> findAll();

    // Delete a Pipeline by ID
    void deleteById(String id);
}
