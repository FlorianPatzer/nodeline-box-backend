package de.nodeline.box.application;

import de.nodeline.box.application.secondaryadapter.DataSinkRepositoryInterface;
import de.nodeline.box.domain.model.DataSink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataSinkService {

    @Autowired
    private DataSinkRepositoryInterface dataSinkRepository;

    public List<DataSink> getAllDataSinks() {
        return new ArrayList<>(dataSinkRepository.findAll());
    }

    public Optional<DataSink> getDataSinkById(UUID id) {
        return dataSinkRepository.findById(id);
    }

    public DataSink createDataSink(DataSink dataSink) {
        return dataSinkRepository.save(dataSink);
    }

    public Optional<DataSink> updateDataSink(UUID id, DataSink dataSink) {
        if(dataSinkRepository.existsById(id)) {
            return Optional.of(dataSinkRepository.save(dataSink));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteDataSink(UUID id) {
        if(dataSinkRepository.existsById(id)) {
            dataSinkRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

