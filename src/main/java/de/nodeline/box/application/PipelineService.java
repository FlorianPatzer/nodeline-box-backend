package de.nodeline.box.application;

import de.nodeline.box.application.secondaryadapter.PipelineRepositoryInterface;
import de.nodeline.box.domain.model.Pipeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PipelineService {

    @Autowired
    private PipelineRepositoryInterface pipelineRepository;

    public List<Pipeline> getAllPipelines() {
        return new ArrayList<>(pipelineRepository.findAll());
    }

    public Optional<Pipeline> getPipelineById(UUID id) {
        return pipelineRepository.findById(id);
    }

    public Pipeline createPipeline(Pipeline pipeline) {
        return pipelineRepository.save(pipeline);
    }

    public Optional<Pipeline> updatePipeline(UUID id, Pipeline pipeline) {
        if(pipelineRepository.existsById(id)) {
            return Optional.of(pipelineRepository.save(pipeline));
        } else {
            return Optional.empty();
        }
    }

    public boolean deletePipeline(UUID id) {
        if(pipelineRepository.existsById(id)) {
            pipelineRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

