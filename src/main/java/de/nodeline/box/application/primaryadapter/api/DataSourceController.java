package de.nodeline.box.application.primaryadapter.api;

import de.nodeline.box.domain.model.DataSource;
import de.nodeline.box.application.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/datasources")
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    @GetMapping
    public ResponseEntity<List<DataSource>> getAllDataSources() {
        return ResponseEntity.ok(dataSourceService.getAllDataSources());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataSource> getDataSourceById(@PathVariable UUID id) {
        Optional<DataSource> dataSource = dataSourceService.getDataSourceById(id);
        return dataSource.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DataSource> createDataSource(@RequestBody DataSource dataSource) {
        DataSource createdDataSource = dataSourceService.createDataSource(dataSource);
        return ResponseEntity.status(201).body(createdDataSource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataSource> updateDataSource(@PathVariable UUID id, @RequestBody DataSource dataSource) {
        Optional<DataSource> updatedDataSource = dataSourceService.updateDataSource(id, dataSource);
        return updatedDataSource.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataSource(@PathVariable UUID id) {
        boolean deleted = dataSourceService.deleteDataSource(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
