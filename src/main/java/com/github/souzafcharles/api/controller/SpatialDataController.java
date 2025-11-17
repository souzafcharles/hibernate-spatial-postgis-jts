package com.github.souzafcharles.api.controller;

import com.github.souzafcharles.api.model.dto.SpatialDataSerializerRequestDTO;
import com.github.souzafcharles.api.model.dto.SpatialDataDeserializerRequestDTO;
import com.github.souzafcharles.api.model.dto.SpatialDataResponseDTO;
import com.github.souzafcharles.api.model.dto.GeoJsonResponseDTO;
import com.github.souzafcharles.api.service.SpatialDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spatial-data")
public class SpatialDataController {

    private final SpatialDataService spatialDataService;

    public SpatialDataController(SpatialDataService spatialDataService) {
        this.spatialDataService = spatialDataService;
    }

    @PostMapping("/serializer")
    public ResponseEntity<SpatialDataResponseDTO> createFromSerializer(@RequestBody SpatialDataSerializerRequestDTO request) {
        SpatialDataResponseDTO response = spatialDataService.createFromSerializerFormat(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deserializer")
    public ResponseEntity<SpatialDataResponseDTO> createFromDeserializer(@RequestBody SpatialDataDeserializerRequestDTO request) {
        SpatialDataResponseDTO response = spatialDataService.createFromDeserializerFormat(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SpatialDataResponseDTO>> getAll() {
        List<SpatialDataResponseDTO> responses = spatialDataService.getAllSpatialData();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpatialDataResponseDTO> getById(@PathVariable Long id) {
        SpatialDataResponseDTO response = spatialDataService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/geojson")
    public ResponseEntity<GeoJsonResponseDTO> getAsGeoJson(@PathVariable Long id) {
        GeoJsonResponseDTO response = spatialDataService.getPolygonAsGeoJson(id);
        return ResponseEntity.ok(response);
    }
}