package de.nodeline.box.application.secondaryadapter;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.nodeline.box.domain.model.Link;

@Repository
public interface PeerToPeerConnectionRepositoryInterface extends JpaRepository<Link, UUID> {
}
