package de.nodeline.box.domain.port.repository;
/**
 * The DataSink entity, like DataSource, connects to Endpoint and operates in a pipeline context.
 */
import de.nodeline.box.domain.model.DataSink;
import java.util.List;
import java.util.Optional;

public interface DataSinkRepository {

    // Save a DataSink
    void save(DataSink dataSink);

    // Retrieve a DataSink by ID
    Optional<DataSink> findById(String id);

    // Retrieve all DataSinks for a given Pipeline ID
    List<DataSink> findByPipelineId(String pipelineId);

    // Retrieve all DataSinks
    List<DataSink> findAll();

    // Delete a DataSink by ID
    void deleteById(String id);
}

