package de.nodeline.box.application.primaryadapter.api;


// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import de.nodeline.box.application.acl.api.TransformationService;
// import de.nodeline.box.application.primaryadapter.api.dto.LinkableDto;

import java.util.List;
// import java.util.Optional;
// import java.util.UUID;

@RestController
@RequestMapping("/api/transformations")
public class TransformationController {

    /* @Autowired
    private TransformationService transformationService; */

    @GetMapping("/types")
    public ResponseEntity<List<String>> getAllTransformationTypes() {
        return ResponseEntity.ok(List.of("JOLT"));
    }

   /*  @GetMapping
    public ResponseEntity<List<LinkableDto>> getAllTransformations() {
        return ResponseEntity.ok(transformationService.getAllTransformations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinkableDto> getTransformationById(@PathVariable UUID id) {
        Optional<LinkableDto> transformation = transformationService.getTransformationById(id);
        return transformation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LinkableDto> createTransformation(@RequestBody LinkableDto transformation) {
        LinkableDto createdTransformation = transformationService.createTransformation(transformation);
        return ResponseEntity.status(201).body(createdTransformation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LinkableDto> updateTransformation(@PathVariable UUID id, @RequestBody LinkableDto transformation) {
        Optional<LinkableDto> updatedTransformation = transformationService.updateTransformation(id, transformation);
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
    } */
}

