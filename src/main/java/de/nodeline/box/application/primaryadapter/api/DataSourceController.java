package de.nodeline.box.application.primaryadapter.api;

// import de.nodeline.box.application.acl.api.DataSourceService;
// import de.nodeline.box.application.primaryadapter.api.dto.DataSourceDto;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;
// import java.util.Optional;
// import java.util.UUID;

@RestController
@RequestMapping("/api/datasources")
public class DataSourceController {

    @Operation(summary = "Get all data source types")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of data source types")
    })
    @GetMapping("/types")
    public ResponseEntity<List<String>> getAllDataSourceTypes() {
        return ResponseEntity.ok(List.of("HTTP_GET"));
    }

    /* @Autowired
    private DataSourceService dataSourceService;

    @GetMapping
    public ResponseEntity<List<DataSourceDto>> getAllDataSources() {
        return ResponseEntity.ok(dataSourceService.getAllDataSources());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataSourceDto> getDataSourceById(@PathVariable UUID id) {
        Optional<DataSourceDto> dataSourceDto = dataSourceService.getDataSourceById(id);
        return dataSourceDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DataSourceDto> createDataSource(@RequestBody DataSourceDto dataSourceDto) {
        DataSourceDto createdDataSource = dataSourceService.createDataSource(dataSourceDto);
        return ResponseEntity.status(201).body(createdDataSource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataSourceDto> updateDataSource(@PathVariable UUID id, @RequestBody DataSourceDto dataSourceDto) {
        Optional<DataSourceDto> updatedDataSource = dataSourceService.updateDataSource(id, dataSourceDto);
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
    } */
}
