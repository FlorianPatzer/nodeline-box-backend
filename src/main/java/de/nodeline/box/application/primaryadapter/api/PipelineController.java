package de.nodeline.box.application.primaryadapter.api;

import de.nodeline.box.application.acl.api.PipelineService;
import de.nodeline.box.application.primaryadapter.api.dto.PipelineDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/pipelines")
public class PipelineController {

    @Autowired
    private PipelineService pipelineService;

    @Operation(summary = "Get all pipelines")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of pipelines"),
        @ApiResponse(responseCode = "204", description = "No pipelines found")
    })
    @GetMapping
    public ResponseEntity<List<PipelineDto>> getAllPipelines() {
        List<PipelineDto> pipelines = pipelineService.getAllPipelines();
        if(pipelines.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pipelines);
    }

    @Operation(summary = "Get a specific pipeline by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pipeline details"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PipelineDto> getPipelineById(@Parameter(description = "UUID of the pipeline")  @PathVariable UUID id) {
        Optional<PipelineDto> pipeline = pipelineService.getPipelineById(id);
        if (pipeline.isPresent()) {
            return ResponseEntity.ok(pipeline.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new pipeline")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pipeline created"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "422", description = "Pipeline creation failed")
    })
    @PostMapping
    public ResponseEntity<PipelineDto> createPipeline(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Pipeline object from the request body")
            @RequestBody PipelineDto pipeline) {
        if (pipeline.getId() != null) {
            return ResponseEntity.badRequest().build();
            
        }
        Optional<PipelineDto> createdPipeline = pipelineService.createPipeline(pipeline);
        if(createdPipeline.isEmpty()) {
            return ResponseEntity.unprocessableEntity().build();
        } else {
            return ResponseEntity.status(201).body(createdPipeline.get());
        }
    }

    @Operation(summary = "Update an existing pipeline by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pipeline updated"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PipelineDto> updatePipeline(@Parameter(description = "UUID of the Pipeline") @PathVariable UUID id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Pipeline object with desired changes") @RequestBody PipelineDto pipeline) {
        Optional<PipelineDto> updatedPipeline = pipelineService.updatePipeline(id, pipeline);
        if (updatedPipeline.isPresent()) {
            return ResponseEntity.ok(updatedPipeline.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a pipeline by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pipeline deleted"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePipeline(@Parameter(description = "UUID of the pipeline") @PathVariable UUID id) {
        boolean deleted = pipelineService.deletePipeline(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

