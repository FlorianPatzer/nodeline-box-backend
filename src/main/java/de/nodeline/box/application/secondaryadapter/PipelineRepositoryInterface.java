package de.nodeline.box.application.secondaryadapter;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import de.nodeline.box.domain.model.Pipeline;

public interface PipelineRepositoryInterface extends JpaRepository<Pipeline, UUID> {
    
}
