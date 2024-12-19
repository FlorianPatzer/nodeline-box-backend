package de.nodeline.box.application.secondaryadapter;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import de.nodeline.box.domain.model.Transformation;

public interface TransformationRepositoryInterface<T extends Transformation> extends JpaRepository<T, UUID> {
}
