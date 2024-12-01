package de.nodeline.box.application.primaryadapter.api;

import de.nodeline.box.domain.model.DataSink;
import de.nodeline.box.application.DataSinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/datasinks")
public class DataSinkController {

    @Autowired
    private DataSinkService dataSinkService;

    @GetMapping
    public ResponseEntity<List<DataSink>> getAllDataSinks() {
        return ResponseEntity.ok(dataSinkService.getAllDataSinks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataSink> getDataSinkById(@PathVariable UUID id) {
        Optional<DataSink> dataSink = dataSinkService.getDataSinkById(id);
        return dataSink.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DataSink> createDataSink(@RequestBody DataSink dataSink) {
        DataSink createdDataSink = dataSinkService.createDataSink(dataSink);
        return ResponseEntity.status(201).body(createdDataSink);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataSink> updateDataSink(@PathVariable UUID id, @RequestBody DataSink dataSink) {
        Optional<DataSink> updatedDataSink = dataSinkService.updateDataSink(id, dataSink);
        return updatedDataSink.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataSink(@PathVariable UUID id) {
        boolean deleted = dataSinkService.deleteDataSink(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

