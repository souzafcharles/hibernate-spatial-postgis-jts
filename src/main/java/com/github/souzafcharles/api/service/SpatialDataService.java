package com.github.souzafcharles.api.service;

import com.github.souzafcharles.api.model.dto.SpatialDataRequestDTO;
import com.github.souzafcharles.api.model.dto.SpatialDataResponseDTO;
import com.github.souzafcharles.api.model.dto.GeoJsonResponseDTO;
import com.github.souzafcharles.api.model.entity.SpatialData;
import com.github.souzafcharles.api.repository.SpatialDataRepository;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpatialDataService {

    private final SpatialDataRepository repository;
    private final GeometryFactory geometryFactory;

    public SpatialDataService(SpatialDataRepository repository) {
        this.repository = repository;
        this.geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    public SpatialDataResponseDTO createSpatialData(SpatialDataRequestDTO request) {
        SpatialData spatialData = new SpatialData();
        // Convert coordinates to JTS geometries
        if (request.pointCoordinates() != null && request.pointCoordinates().size() >= 2) {
            spatialData.setPoint(createPoint(request.pointCoordinates()));
        }
        if (request.polygonCoordinates() != null && !request.polygonCoordinates().isEmpty()) {
            spatialData.setPolygon(createPolygon(request.polygonCoordinates()));
        }
        // Save to database
        SpatialData saved = repository.save(spatialData);
        return toResponse(saved);
    }

    public List<SpatialDataResponseDTO> getAllSpatialData() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SpatialDataResponseDTO getById(Long id) {
        SpatialData spatialData = repository.findById(id).orElseThrow(() -> new RuntimeException("SpatialData not found with id: " + id));
        return toResponse(spatialData);
    }

    public GeoJsonResponseDTO getPolygonAsGeoJson(Long id) {
        SpatialData spatialData = repository.findById(id).orElseThrow(() -> new RuntimeException("SpatialData not found with id: " + id));
        if (spatialData.getPolygon() == null) {
            throw new RuntimeException("SpatialData with id " + id + " does not have a polygon");
        }
        return new GeoJsonResponseDTO(
                "Feature",
                convertToGeoJsonGeometry(spatialData.getPolygon()),
                new GeoJsonProperties("Polygon from database", id)
        );
    }

    // Helper methods to create geometries
    private Point createPoint(List<Double> coordinates) {
        Coordinate coordinate = new Coordinate(coordinates.get(0), coordinates.get(1));
        return geometryFactory.createPoint(coordinate);
    }

    private Polygon createPolygon(List<List<List<Double>>> polygonCoordinates) {
        if (polygonCoordinates.isEmpty() || polygonCoordinates.get(0).size() < 4) {
            throw new IllegalArgumentException("Polygon must have at least 4 points (first and last must be equal)"
            );
        }
        // Create shell (outer ring)
        Coordinate[] shellCoordinates = polygonCoordinates.get(0).stream()
                .map(coord -> new Coordinate(coord.get(0), coord.get(1)))
                .toArray(Coordinate[]::new);
        LinearRing shell = geometryFactory.createLinearRing(shellCoordinates);
        return geometryFactory.createPolygon(shell);
    }

    private SpatialDataResponseDTO toResponse(SpatialData entity) {
        return new SpatialDataResponseDTO(
                entity.getId(),
                entity.getPoint(),
                entity.getMultiPoint(),
                entity.getLineString(),
                entity.getMultiLineString(),
                entity.getPolygon(),
                entity.getMultiPolygon(),
                entity.getGeometryCollection()
        );
    }

    private Object convertToGeoJsonGeometry(Geometry geometry) {
        // Simple conversion to GeoJSON structure
        if (geometry instanceof Polygon polygon) {
            return createGeoJsonPolygon(polygon);
        }
        return null;
    }

    private Object createGeoJsonPolygon(Polygon polygon) {
        Coordinate[] coordinates = polygon.getExteriorRing().getCoordinates();
        double[][][] polygonCoordinates = new double[1][coordinates.length][2];
        for (int i = 0; i < coordinates.length; i++) {
            polygonCoordinates[0][i][0] = coordinates[i].getX();
            polygonCoordinates[0][i][1] = coordinates[i].getY();
        }
        return new GeoJsonGeometry("Polygon", polygonCoordinates);
    }

    // Helper records for GeoJSON structure
    private record GeoJsonGeometry(String type, double[][][] coordinates) {}
    private record GeoJsonProperties(String description, Long id) {}
}
