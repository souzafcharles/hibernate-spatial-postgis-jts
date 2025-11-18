package com.github.souzafcharles.api.service;

import com.github.souzafcharles.api.model.dto.SpatialDataSerializerRequestDTO;
import com.github.souzafcharles.api.model.dto.SpatialDataDeserializerRequestDTO;
import com.github.souzafcharles.api.model.dto.SpatialDataResponseDTO;
import com.github.souzafcharles.api.model.dto.GeoJsonResponseDTO;
import com.github.souzafcharles.api.model.entity.SpatialData;
import com.github.souzafcharles.api.repository.SpatialDataRepository;
import com.github.souzafcharles.api.utils.Messages;
import jakarta.persistence.EntityNotFoundException;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpatialDataService {

    private final SpatialDataRepository spatialDataRepository;
    private final GeometryFactory geometryFactory;

    public SpatialDataService(SpatialDataRepository spatialDataRepository) {
        this.spatialDataRepository = spatialDataRepository;
        this.geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    // ------------------------------------------------------------
    // CREATE – From Serializer Format (lists of coordinates)
    // ------------------------------------------------------------
    public SpatialDataResponseDTO createFromSerializerFormat(SpatialDataSerializerRequestDTO request) {
        SpatialData spatialData = new SpatialData();
        if (hasValidPointCoordinates(request.point())) {
            spatialData.setPoint(createPoint(request.point()));
        }
        if (hasValidPolygonCoordinates(request.polygon())) {
            spatialData.setPolygon(createPolygon(request.polygon()));
        }
        if (hasValidLineStringCoordinates(request.linestring())) {
            spatialData.setLineString(createLineString(request.linestring()));
        }
        SpatialData savedEntity = spatialDataRepository.save(spatialData);
        return toResponse(savedEntity);
    }

    // ------------------------------------------------------------
    // CREATE – From Deserializer Format (JTS Geometry objects)
    // ------------------------------------------------------------
    public SpatialDataResponseDTO createFromDeserializerFormat(SpatialDataDeserializerRequestDTO request) {
        SpatialData spatialData = new SpatialData();
        if (request.point() != null) {
            spatialData.setPoint((Point) request.point());
        }
        if (request.polygon() != null) {
            spatialData.setPolygon((Polygon) request.polygon());
        }
        if (request.linestring() != null) {
            spatialData.setLineString((LineString) request.linestring());
        }
        SpatialData savedEntity = spatialDataRepository.save(spatialData);
        return toResponse(savedEntity);
    }

    // ------------------------------------------------------------
    // READ – List all
    // ------------------------------------------------------------
    public List<SpatialDataResponseDTO> getAllSpatialData() {
        List<SpatialData> allSpatialData = spatialDataRepository.findAll();
        List<SpatialDataResponseDTO> responses = new ArrayList<>();
        for (SpatialData spatialData : allSpatialData) {
            responses.add(toResponse(spatialData));
        }
        return responses;
    }

    // ------------------------------------------------------------
    // READ – Find by ID
    // ------------------------------------------------------------
    public SpatialDataResponseDTO getById(Long spatialDataId) {
        SpatialData spatialData = spatialDataRepository.findById(spatialDataId).orElseThrow(() -> new EntityNotFoundException(String.format(Messages.SPATIAL_DATA_NOT_FOUND, spatialDataId)));
        return toResponse(spatialData);
    }

    // ------------------------------------------------------------
    // READ – GeoJSON (Polygon only)
    // ------------------------------------------------------------
    public GeoJsonResponseDTO getPolygonAsGeoJson(Long spatialDataId) {
        SpatialData spatialData = spatialDataRepository.findById(spatialDataId).orElseThrow(() -> new EntityNotFoundException(String.format(Messages.SPATIAL_DATA_NOT_FOUND, spatialDataId)));
        if (spatialData.getPolygon() == null) {
            throw new IllegalArgumentException(
                    String.format(Messages.NO_POLYGON_FOUND, spatialDataId)
            );
        }
        return new GeoJsonResponseDTO("Feature", convertToGeoJsonGeometry(spatialData.getPolygon()), new GeoJsonProperties("Polygon from database", spatialDataId));
    }

    // ------------------------------------------------------------
    // GEOMETRY CREATION (COORDINATES → JTS)
    // ------------------------------------------------------------
    private boolean hasValidPointCoordinates(List<Double> coordinates) {
        return coordinates != null && coordinates.size() >= 2;
    }

    private boolean hasValidPolygonCoordinates(List<List<List<Double>>> coordinates) {
        return coordinates != null && !coordinates.isEmpty();
    }

    private boolean hasValidLineStringCoordinates(List<List<Double>> coordinates) {
        return coordinates != null && !coordinates.isEmpty();
    }

    private Point createPoint(List<Double> coordinates) {
        return geometryFactory.createPoint(
                new Coordinate(coordinates.get(0), coordinates.get(1))
        );
    }

    private Polygon createPolygon(List<List<List<Double>>> polygonCoordinates) {
        List<List<Double>> exteriorRing = polygonCoordinates.get(0);
        if (exteriorRing.size() < 4) {
            throw new IllegalArgumentException(Messages.INVALID_POLYGON_COORDINATES);
        }
        Coordinate[] shellCoordinates = new Coordinate[exteriorRing.size()];
        for (int i = 0; i < exteriorRing.size(); i++) {
            shellCoordinates[i] = new Coordinate(
                    exteriorRing.get(i).get(0),
                    exteriorRing.get(i).get(1)
            );
        }
        LinearRing shell = geometryFactory.createLinearRing(shellCoordinates);
        return geometryFactory.createPolygon(shell);
    }

    private LineString createLineString(List<List<Double>> lineCoordinates) {
        Coordinate[] coordinates = new Coordinate[lineCoordinates.size()];
        for (int i = 0; i < lineCoordinates.size(); i++) {
            coordinates[i] = new Coordinate(lineCoordinates.get(i).get(0), lineCoordinates.get(i).get(1));
        }
        return geometryFactory.createLineString(coordinates);
    }

    // ------------------------------------------------------------
    // CONVERT JTS → GEOJSON
    // ------------------------------------------------------------
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
        double[][][] polygonCoordinates = new double[1][coordinates.length][2];
        for (int i = 0; i < coordinates.length; i++) {
            polygonCoordinates[0][i][0] = coordinates[i].x;
            polygonCoordinates[0][i][1] = coordinates[i].y;
        }
        return new GeoJsonGeometry("Polygon", polygonCoordinates);
    }

    private Object createGeoJsonPoint(Point point) {
        return new GeoJsonGeometry("Point", new double[]{point.getX(), point.getY()});
    }

    private Object createGeoJsonLineString(LineString lineString) {
        Coordinate[] coordinates = lineString.getCoordinates();
        double[][] lineCoordinates = new double[coordinates.length][2];
        for (int i = 0; i < coordinates.length; i++) {
            lineCoordinates[i][0] = coordinates[i].x;
            lineCoordinates[i][1] = coordinates[i].y;
        }
        return new GeoJsonGeometry("LineString", lineCoordinates);
    }

    // ------------------------------------------------------------
    // MAPPER
    // ------------------------------------------------------------
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

    // ------------------------------------------------------------
    // Internal Helper Records
    // ------------------------------------------------------------
    private record GeoJsonGeometry(String type, Object coordinates) {}
    private record GeoJsonProperties(String description, Long id) {}
}