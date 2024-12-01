package de.nodeline.box.application;

import de.nodeline.box.application.secondaryadapter.DataSourceRepositoryInterface;
import de.nodeline.box.domain.model.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataSourceService {

    @Autowired
    private DataSourceRepositoryInterface dataSourceRepository;

    public List<DataSource> getAllDataSources() {
        return new ArrayList<>(dataSourceRepository.findAll());
    }

    public Optional<DataSource> getDataSourceById(UUID id) {
        return dataSourceRepository.findById(id);
    }

    public DataSource createDataSource(DataSource dataSource) {
        return dataSourceRepository.save(dataSource);
    }

    public Optional<DataSource> updateDataSource(UUID id, DataSource dataSource) {
        if(dataSourceRepository.existsById(id)) {
            return Optional.of(dataSourceRepository.save(dataSource));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteDataSource(UUID id) {
        if(dataSourceRepository.existsById(id)) {
            dataSourceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

