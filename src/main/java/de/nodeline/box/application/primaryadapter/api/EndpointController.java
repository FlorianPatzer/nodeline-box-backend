package de.nodeline.box.application.primaryadapter.api;


import de.nodeline.box.domain.model.Endpoint;
import de.nodeline.box.application.EndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/endpoints")
public class EndpointController {

    @Autowired
    private EndpointService endpointService;

    @GetMapping
    public ResponseEntity<List<Endpoint>> getAllEndpoints() {
        return ResponseEntity.ok(endpointService.getAllEndpoints());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endpoint> getEndpointById(@PathVariable UUID id) {
        Optional<Endpoint> endpoint = endpointService.getEndpointById(id);
        return endpoint.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Endpoint> createEndpoint(@RequestBody Endpoint endpoint) {
        Endpoint createdEndpoint = endpointService.createEndpoint(endpoint);
        return ResponseEntity.status(201).body(createdEndpoint);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Endpoint> updateEndpoint(@PathVariable UUID id, @RequestBody Endpoint endpoint) {
        Optional<Endpoint> updatedEndpoint = endpointService.updateEndpoint(id, endpoint);
        return updatedEndpoint.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEndpoint(@PathVariable UUID id) {
        boolean deleted = endpointService.deleteEndpoint(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
