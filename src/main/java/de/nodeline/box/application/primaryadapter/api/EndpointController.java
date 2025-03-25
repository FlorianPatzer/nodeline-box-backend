package de.nodeline.box.application.primaryadapter.api;


import de.nodeline.box.application.acl.api.RestEndpointService;
import de.nodeline.box.application.primaryadapter.api.dto.EndpointDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/endpoints")
public class EndpointController {

    @Autowired
    private RestEndpointService endpointService;

    @Operation(summary = "Get all endpoints")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of endpoints")
    })
    @GetMapping
    public ResponseEntity<List<EndpointDto>> getAllEndpoints() {
        return ResponseEntity.ok(endpointService.getAllEndpoints());
    }

    @Operation(summary = "Get an endpoint by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The endpoint with the given ID"),
        @ApiResponse(responseCode = "404", description = "No endpoint with given ID found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EndpointDto> getEndpointById(@PathVariable UUID id) {
        EndpointDto endpoint = endpointService.getEndpointById(id);
        return ResponseEntity.ok(endpoint);
    }

    @Operation(summary = "Create an endpoint")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Endpoint created")
    })
    @PostMapping
    public ResponseEntity<EndpointDto> createEndpoint(@RequestBody EndpointDto endpoint) {
        EndpointDto createdEndpoint = endpointService.createEndpoint(endpoint);
        return ResponseEntity.status(201).body(createdEndpoint);
    }

    @Operation(summary = "Update an endpoint")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endpoint updated"),
        @ApiResponse(responseCode = "404", description = "Endpoint not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EndpointDto> updateEndpoint(@PathVariable UUID id, @RequestBody EndpointDto endpoint) {
        EndpointDto updatedEndpoint = endpointService.updateEndpoint(id, endpoint);
        return ResponseEntity.ok(updatedEndpoint);
    }

    @Operation(summary = "Delete an endpoint")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Endpoint deleted"),
        @ApiResponse(responseCode = "404", description = "Endpoint not found")
    })
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

