package de.nodeline.box.application.secondaryadapter;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.nodeline.box.application.secondaryadapter.nifi.model.ProcessGroup;

@Repository
public interface NifiProcessGroupRepositoryInterface extends JpaRepository<ProcessGroup, String> {
    public ProcessGroup findByPipelineId(UUID id);
    public boolean existsByPipelineId(UUID id);
    public ProcessGroup getReferenceByPipelineId(UUID id);
    public void deleteByPipelineId(UUID id);
}
