package de.nodeline.box.application.primaryadapter.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import de.nodeline.box.application.acl.TransformationService;
import de.nodeline.box.application.primaryadapter.api.dto.LinkableDto;
import de.nodeline.box.domain.model.Transformation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/transformations")
public class TransformationController {

    @Autowired
    private JoltTransformationService joltTransformationService;

    private TransformationService getServiceByType(LinkableDto.Type type) {
        switch (type) {
            case LinkableDto.Type.JOLT_TRANSFORMATION:
                return joltTransformationService;
                break;
        
            default:
                throw new RuntimeException("No service defined for type " + type);
        }
    }

    @GetMapping
    @RequestMapping("/jolt")
    public ResponseEntity<List<Transformation>> getAllTransformations() {
        return ResponseEntity.ok(getServiceByType(LinkableDto.Type.JOLT_TRANSFORMATION).getAllTransformations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transformation> getTransformationById(@PathVariable UUID id) {
        Optional<Transformation> transformation = transformationService.getTransformationById(id);
        return transformation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Transformation> createTransformation(@RequestBody Transformation transformation) {
        Transformation createdTransformation = transformationService.createTransformation(transformation);
        return ResponseEntity.status(201).body(createdTransformation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transformation> updateTransformation(@PathVariable UUID id, @RequestBody Transformation transformation) {
        Optional<Transformation> updatedTransformation = transformationService.updateTransformation(id, transformation);
        return updatedTransformation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransformation(@PathVariable UUID id) {
        boolean deleted = transformationService.deleteTransformation(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
