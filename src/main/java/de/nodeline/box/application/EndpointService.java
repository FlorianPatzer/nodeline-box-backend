package de.nodeline.box.application;

import de.nodeline.box.application.secondaryadapter.EndpointRepositoryInterface;
import de.nodeline.box.domain.model.Endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EndpointService {

    @Autowired
    private EndpointRepositoryInterface endpointRepository;

    public List<Endpoint> getAllEndpoints() {
        return new ArrayList<>(endpointRepository.findAll());
    }

    public Optional<Endpoint> getEndpointById(UUID id) {
        return endpointRepository.findById(id);
    }

    public Endpoint createEndpoint(Endpoint endpoint) {
        return endpointRepository.save(endpoint);
    }

    public Optional<Endpoint> updateEndpoint(UUID id, Endpoint endpoint) {
        if(endpointRepository.existsById(id)) {
            return Optional.of(endpointRepository.save(endpoint));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteEndpoint(UUID id) {
        if(endpointRepository.existsById(id)) {
            endpointRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

