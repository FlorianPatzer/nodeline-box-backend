package de.nodeline.box.domain.port.repository;

import java.util.List;

import de.nodeline.box.domain.model.Credentials;

public interface CredentialsRepository {
    void save(Credentials credentials);
    Credentials findById(String id);
    List<Credentials> findByEndpointId(String endpointId);
    List<Credentials> findAll();
    void deleteById(String id);
    void deleteByEndpointId(String endpointId);
}
