package de.nodeline.box.application.primaryadapter.api;


import de.nodeline.box.application.acl.EndpointService;
import de.nodeline.box.application.primaryadapter.api.dto.EndpointDto;

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
    public ResponseEntity<List<EndpointDto>> getAllEndpoints() {
        return ResponseEntity.ok(endpointService.getAllEndpoints());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EndpointDto> getEndpointById(@PathVariable UUID id) {
        Optional<EndpointDto> endpoint = endpointService.getEndpointById(id);
        return endpoint.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EndpointDto> createEndpoint(@RequestBody EndpointDto endpoint) {
        EndpointDto createdEndpoint = endpointService.createEndpoint(endpoint);
        return ResponseEntity.status(201).body(createdEndpoint);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EndpointDto> updateEndpoint(@PathVariable UUID id, @RequestBody EndpointDto endpoint) {
        Optional<EndpointDto> updatedEndpoint = endpointService.updateEndpoint(id, endpoint);
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

