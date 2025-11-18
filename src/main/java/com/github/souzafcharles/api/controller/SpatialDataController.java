package com.github.souzafcharles.api.controller;

import com.github.souzafcharles.api.model.dto.SpatialDataSerializerRequestDTO;
import com.github.souzafcharles.api.model.dto.SpatialDataDeserializerRequestDTO;
import com.github.souzafcharles.api.model.dto.SpatialDataResponseDTO;
import com.github.souzafcharles.api.model.dto.GeoJsonResponseDTO;
import com.github.souzafcharles.api.service.SpatialDataService;
import com.github.souzafcharles.api.utils.Messages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spatial-data")
@Tag(name = Messages.TAG_NAME, description = Messages.TAG_DESCRIPTION)
public class SpatialDataController {

    private final SpatialDataService spatialDataService;

    public SpatialDataController(SpatialDataService spatialDataService) {
        this.spatialDataService = spatialDataService;
    }

    @PostMapping("/serializer")
    @Operation(summary = Messages.CREATE_SERIALIZER_SUMMARY, description = Messages.CREATE_SERIALIZER_DESCRIPTION)
    public ResponseEntity<SpatialDataResponseDTO> createFromSerializer(@Valid @RequestBody SpatialDataSerializerRequestDTO request) {
        SpatialDataResponseDTO response = spatialDataService.createFromSerializerFormat(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deserializer")
    @Operation(summary = Messages.CREATE_DESERIALIZER_SUMMARY, description = Messages.CREATE_DESERIALIZER_DESCRIPTION)
    public ResponseEntity<SpatialDataResponseDTO> createFromDeserializer(@Valid @RequestBody SpatialDataDeserializerRequestDTO request) {
        SpatialDataResponseDTO response = spatialDataService.createFromDeserializerFormat(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = Messages.GET_ALL_SUMMARY, description = Messages.GET_ALL_DESCRIPTION)
    public ResponseEntity<List<SpatialDataResponseDTO>> getAll() {
        List<SpatialDataResponseDTO> responses = spatialDataService.getAllSpatialData();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = Messages.GET_BY_ID_SUMMARY, description = Messages.GET_BY_ID_DESCRIPTION)
    public ResponseEntity<SpatialDataResponseDTO> getById(@PathVariable Long id) {
        SpatialDataResponseDTO response = spatialDataService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/geojson")
    @Operation(summary = Messages.GET_GEOJSON_SUMMARY, description = Messages.GET_GEOJSON_DESCRIPTION)
    public ResponseEntity<GeoJsonResponseDTO> getAsGeoJson(@PathVariable Long id) {
        GeoJsonResponseDTO response = spatialDataService.getPolygonAsGeoJson(id);
        return ResponseEntity.ok(response);
    }
}