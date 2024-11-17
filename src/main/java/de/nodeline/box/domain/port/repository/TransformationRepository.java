package de.nodeline.box.domain.port.repository;

/**
 * The Transformation aggregate includes InputPort and OutputPort
 * and may have specific implementations like JoltTransformation.
 */
import de.nodeline.box.domain.model.Transformation;
import java.util.List;
import java.util.Optional;

public interface TransformationRepository {

    // Save a Transformation
    void save(Transformation transformation);

    // Retrieve a Transformation by ID
    Optional<Transformation> findById(String id);

    // Retrieve all Transformations
    List<Transformation> findAll();

    // Delete a Transformation by ID
    void deleteById(String id);
}

