package de.nodeline.box.domain.port.repository;

/**
 * The NodelineBox aggregate root includes its associated Pipeline objects.
 * This repository handles all operations related to the NodelineBox.
 */

import de.nodeline.box.domain.model.NodelineBox;
import java.util.List;
import java.util.Optional;
public interface NodelineBoxRepository {
    // Save a NodeLineBox
    void save(NodelineBox NodelineBox);

    // Retrieve a NodelineBox by ID
    Optional<NodelineBox> findById(String id);

    // Retrieve all NodelineBoxes
    List<NodelineBox> findAll();

    // Delete a NodelineBox by ID
    void deleteById(String id);
}
