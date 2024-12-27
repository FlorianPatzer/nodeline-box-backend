package de.nodeline.box.application.primaryadapter.api;

import de.nodeline.box.application.acl.api.PipelineService;
import de.nodeline.box.application.primaryadapter.api.dto.PipelineDto;

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

    /**
     * Get all pipelines
     *
     * @return List of pipelines
     */
    @GetMapping
    public ResponseEntity<List<PipelineDto>> getAllPipelines() {
        List<PipelineDto> pipelines = pipelineService.getAllPipelines();
        return ResponseEntity.ok(pipelines);
    }

    /**
     * Get a specific pipeline by ID
     *
     * @param id UUID of the pipeline
     * @return Pipeline details or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<PipelineDto> getPipelineById(@PathVariable UUID id) {
        Optional<PipelineDto> pipeline = pipelineService.getPipelineById(id);
        if (pipeline.isPresent()) {
            return ResponseEntity.ok(pipeline.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create a new pipeline
     *
     * @param pipeline Pipeline object from the request body
     * @return Created pipeline
     */
    @PostMapping
    public ResponseEntity<PipelineDto> createPipeline(@RequestBody PipelineDto pipeline) {
        PipelineDto createdPipeline = pipelineService.createPipeline(pipeline);
        return ResponseEntity.status(201).body(createdPipeline);
    }

    /**
     * Update an existing pipeline by ID
     *
     * @param id       UUID of the pipeline
     * @param pipeline Updated pipeline object
     * @return Updated pipeline or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<PipelineDto> updatePipeline(@PathVariable UUID id, @RequestBody PipelineDto pipeline) {
        Optional<PipelineDto> updatedPipeline = pipelineService.updatePipeline(id, pipeline);
        if (updatedPipeline.isPresent()) {
            return ResponseEntity.ok(updatedPipeline.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a pipeline by ID
     *
     * @param id UUID of the pipeline
     * @return No content on successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePipeline(@PathVariable UUID id) {
        boolean deleted = pipelineService.deletePipeline(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

