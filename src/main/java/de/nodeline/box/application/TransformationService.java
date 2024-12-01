package de.nodeline.box.application;

import de.nodeline.box.application.secondaryadapter.TransformationRepositoryInterface;
import de.nodeline.box.domain.model.Transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransformationService {

    @Autowired
    private TransformationRepositoryInterface transformationRepository;

    public List<Transformation> getAllTransformations() {
        return new ArrayList<>(transformationRepository.findAll());
    }

    public Optional<Transformation> getTransformationById(UUID id) {
        return transformationRepository.findById(id);
    }

    public Transformation createTransformation(Transformation transformation) {
        return transformationRepository.save(transformation);
    }

    public Optional<Transformation> updateTransformation(UUID id, Transformation transformation) {
        if(transformationRepository.existsById(id)) {
            return Optional.of(transformationRepository.save(transformation));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteTransformation(UUID id) {
        if(transformationRepository.existsById(id)) {
            transformationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

