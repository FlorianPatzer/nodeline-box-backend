package de.nodeline.box.domain.port.repository;

/**
 * The DataSource entity connects to Endpoint and can be queried based on its associated Pipeline.
 */
import de.nodeline.box.domain.model.DataSource;
import java.util.List;
import java.util.Optional;

public interface DataSourceRepository {

    // Save a DataSource
    void save(DataSource dataSource);

    // Retrieve a DataSource by ID
    Optional<DataSource> findById(String id);

    // Retrieve all DataSources for a given Pipeline ID
    List<DataSource> findByPipelineId(String pipelineId);

    // Retrieve all DataSources
    List<DataSource> findAll();

    // Delete a DataSource by ID
    void deleteById(String id);
}

