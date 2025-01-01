package de.nodeline.box.application.secondaryadapter;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.nodeline.box.application.primaryadapter.nifi.model.ProcessGroup;

@Repository
public interface NifiProcessGroupRepositoryInterface extends JpaRepository<ProcessGroup, String> {
    public ProcessGroup findByPipelineId(UUID id);
}
