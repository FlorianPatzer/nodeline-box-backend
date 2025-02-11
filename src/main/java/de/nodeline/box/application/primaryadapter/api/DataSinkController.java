package de.nodeline.box.application.primaryadapter.api;

// import de.nodeline.box.application.acl.api.DataSinkService;
// import de.nodeline.box.application.primaryadapter.api.dto.DataSinkDto;

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
@RequestMapping("/api/datasinks")
public class DataSinkController {

    @Operation(summary = "Get all data sink types")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of data sink types")
    })
    @GetMapping("/types")
    public ResponseEntity<List<String>> getAllDataSinkTypes() {
        return ResponseEntity.ok(List.of("HTTP_POST"));
    }

    /* @Autowired
    private DataSinkService dataSinkService;

    @GetMapping
    public ResponseEntity<List<DataSinkDto>> getAllDataSinks() {
        return ResponseEntity.ok(dataSinkService.getAllDataSinks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataSinkDto> getDataSinkById(@PathVariable UUID id) {
        Optional<DataSinkDto> dataSink = dataSinkService.getDataSinkById(id);
        return dataSink.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DataSinkDto> createDataSink(@RequestBody DataSinkDto dataSinkDto) {
        DataSinkDto createdDataSink = dataSinkService.createDataSink(dataSinkDto);
        return ResponseEntity.status(201).body(createdDataSink);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataSinkDto> updateDataSink(@PathVariable UUID id, @RequestBody DataSinkDto dataSinkDto) {
        Optional<DataSinkDto> updatedDataSink = dataSinkService.updateDataSink(id, dataSinkDto);
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
    } */
}

