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
        if (request.pointCoordinates() != null && request.pointCoordinates().size() >= 2) {
            spatialData.setPoint(createPoint(request.pointCoordinates()));
        }
        if (request.polygonCoordinates() != null && !request.polygonCoordinates().isEmpty()) {
            spatialData.setPolygon(createPolygon(request.polygonCoordinates()));
        }
        SpatialData saved = repository.save(spatialData);
        return toResponse(saved);
    }

    public SpatialDataResponseDTO createFromGeoJson(String geoJson) {
        throw new UnsupportedOperationException("GeoJSON parsing not implemented yet");
    }

    public List<SpatialDataResponseDTO> getAllSpatialData() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SpatialDataResponseDTO getById(Long id) {
        SpatialData spatialData = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SpatialData not found with id: " + id));

        return toResponse(spatialData);
    }

    public GeoJsonResponseDTO getPolygonAsGeoJson(Long id) {
        SpatialData spatialData = repository.findById(id).orElseThrow(() -> new RuntimeException("SpatialData not found with id: " + id));
        if (spatialData.getPolygon() == null) {
            throw new RuntimeException("SpatialData with id " + id + " does not contain a polygon");
        }
        return new GeoJsonResponseDTO(
                "Feature",
                convertToGeoJsonGeometry(spatialData.getPolygon()),
                new GeoJsonProperties("Polygon from database", id)
        );
    }

    private Object convertToGeoJsonGeometry(Geometry geometry) {
        if (geometry instanceof Polygon polygon) {
            return createGeoJsonPolygon(polygon);
        }
        if (geometry instanceof Point point) {
            return createGeoJsonPoint(point);
        }
        if (geometry instanceof LineString lineString) {
            return createGeoJsonLineString(lineString);
        }
        return null;
    }

    private Object createGeoJsonPolygon(Polygon polygon) {
        Coordinate[] coordinates = polygon.getExteriorRing().getCoordinates();
        double[][][] polygonCoords = new double[1][coordinates.length][2];
        for (int i = 0; i < coordinates.length; i++) {
            polygonCoords[0][i][0] = coordinates[i].x;
            polygonCoords[0][i][1] = coordinates[i].y;
        }
        return new GeoJsonGeometry("Polygon", polygonCoords);
    }

    private Object createGeoJsonPoint(Point point) {
        return new GeoJsonGeometry(
                "Point",
                new double[]{point.getX(), point.getY()}
        );
    }

    private Object createGeoJsonLineString(LineString lineString) {
        Coordinate[] coordinates = lineString.getCoordinates();
        double[][] lineCoords = new double[coordinates.length][2];
        for (int i = 0; i < coordinates.length; i++) {
            lineCoords[i][0] = coordinates[i].x;
            lineCoords[i][1] = coordinates[i].y;
        }
        return new GeoJsonGeometry("LineString", lineCoords);
    }

    private Point createPoint(List<Double> coords) {
        return geometryFactory.createPoint(new Coordinate(coords.get(0), coords.get(1)));
    }

    private Polygon createPolygon(List<List<List<Double>>> polygonCoords) {
        if (polygonCoords.isEmpty() || polygonCoords.get(0).size() < 4) {
            throw new IllegalArgumentException("Polygon must contain at least 4 points (first and last must match)");
        }
        Coordinate[] shellCoordinates = polygonCoords.get(0).stream()
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

    private record GeoJsonGeometry(String type, Object coordinates) {}
    private record GeoJsonProperties(String description, Long id) {}
}
