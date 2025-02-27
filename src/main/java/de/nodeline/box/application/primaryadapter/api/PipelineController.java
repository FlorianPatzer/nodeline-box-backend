package de.nodeline.box.application.primaryadapter.api;

import de.nodeline.box.application.acl.api.PipelineService;
import de.nodeline.box.application.primaryadapter.api.dto.PipelineDto;
import de.nodeline.box.application.primaryadapter.api.dto.PipelineStatusDto;
import de.nodeline.box.domain.model.PipelineStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        PipelineDto pipeline = pipelineService.getPipelineById(id);
        return ResponseEntity.ok(pipeline);        
    }

    @Operation(summary = "Create a new pipeline")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pipeline created"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
    })
    @PostMapping
    public ResponseEntity<PipelineDto> createPipeline(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Pipeline object from the request body")
            @RequestBody PipelineDto pipeline) {
        PipelineDto createdPipeline = pipelineService.createPipeline(pipeline);
        return ResponseEntity.status(201).body(createdPipeline);
    }

    @Operation(summary = "Change the status of a pipeline")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pipeline status updated"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "500", description = "Unable to update pipeline")
    })

    @PostMapping("/{id}/status")
    public ResponseEntity<Void> changePipelineStatus(@Parameter(description = "UUID of the Pipeline") @PathVariable UUID id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Pipeline status")
            @RequestBody PipelineStatusDto pipelineStatus) {
        pipelineService.updatePipelineStatus(id, pipelineStatus);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update an existing pipeline by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pipeline updated"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "500", description = "Unable to update pipeline")
    })
    
    @PutMapping("/{id}")
    public ResponseEntity<PipelineDto> updatePipeline(@Parameter(description = "UUID of the Pipeline") @PathVariable UUID id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Pipeline object with desired changes") @RequestBody PipelineDto pipeline) {
        PipelineDto updatedPipeline = pipelineService.updatePipeline(id, pipeline);
        return ResponseEntity.ok(updatedPipeline);
    }

    @Operation(summary = "Delete a pipeline by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pipeline deleted"),
        @ApiResponse(responseCode = "404", description = "Pipeline not found"),
        @ApiResponse(responseCode = "500", description = "Unable to delete pipeline")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePipeline(@Parameter(description = "UUID of the pipeline") @PathVariable UUID id) {
        pipelineService.deletePipeline(id);
        return ResponseEntity.ok().build();        
    }
}

