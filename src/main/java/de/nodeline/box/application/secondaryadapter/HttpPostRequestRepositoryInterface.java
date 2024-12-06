package de.nodeline.box.application.secondaryadapter;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.nodeline.box.domain.model.HttpPostRequest;


@Repository
public interface HttpPostRequestRepositoryInterface extends JpaRepository<HttpPostRequest, UUID> {
}
