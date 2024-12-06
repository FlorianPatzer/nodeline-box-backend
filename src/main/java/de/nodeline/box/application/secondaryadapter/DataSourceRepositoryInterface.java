package de.nodeline.box.application.secondaryadapter;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import de.nodeline.box.domain.model.DataSource;


public interface DataSourceRepositoryInterface  extends JpaRepository<DataSource, UUID>{
}
