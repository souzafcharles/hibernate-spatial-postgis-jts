package com.github.souzafcharles.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.locationtech.jts.geom.*;

import java.io.IOException;

public class GeometryDeserializer extends JsonDeserializer<Geometry> {

    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Override
    public Geometry deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode rootNode = parser.getCodec().readTree(parser);
        if (rootNode.isNull()) {
            return null;
        }
        String geometryType = rootNode.get("type").asText();
        JsonNode coordinatesNode = rootNode.get("coordinates");
        return switch (geometryType) {
            case "Point" -> deserializePoint(coordinatesNode);
            case "Polygon" -> deserializePolygon(coordinatesNode);
            case "LineString" -> deserializeLineString(coordinatesNode);
            default -> throw new IllegalArgumentException("Unsupported geometry type: " + geometryType);
        };
    }

    // Deserializes a GeoJSON Point structure into a JTS Point.
    private Point deserializePoint(JsonNode coordinatesNode) {
        if (coordinatesNode == null || !coordinatesNode.isArray() || coordinatesNode.size() < 2) {
            return null;
        }
        double x = coordinatesNode.get(0).asDouble();
        double y = coordinatesNode.get(1).asDouble();
        return geometryFactory.createPoint(new Coordinate(x, y));
    }

    // Deserializes a GeoJSON Polygon structure into a JTS Polygon. Only the exterior ring (shell) is processed here.
    private Polygon deserializePolygon(JsonNode coordinatesNode) {
        if (coordinatesNode == null || !coordinatesNode.isArray() || coordinatesNode.size() == 0) {
            return null;
        }
        // The first element represents the exterior ring
        JsonNode exteriorRingNode = coordinatesNode.get(0);
        Coordinate[] exteriorCoordinates = parseCoordinateArray(exteriorRingNode);
        if (exteriorCoordinates.length < 4) {
            throw new IllegalArgumentException("Polygon must contain at least 4 coordinates (closed ring)");
        }
        LinearRing shell = geometryFactory.createLinearRing(exteriorCoordinates);
        return geometryFactory.createPolygon(shell);
    }

    // Deserializes a GeoJSON LineString structure into a JTS LineString.
    private LineString deserializeLineString(JsonNode coordinatesNode) {
        if (coordinatesNode == null || !coordinatesNode.isArray()) {
            return null;
        }
        Coordinate[] coordinates = parseCoordinateArray(coordinatesNode);
        return geometryFactory.createLineString(coordinates);
    }

    // Parses an array of coordinate pairs into a JTS Coordinate array.
    private Coordinate[] parseCoordinateArray(JsonNode coordinateArrayNode) {
        Coordinate[] coordinates = new Coordinate[coordinateArrayNode.size()];
        for (int i = 0; i < coordinateArrayNode.size(); i++) {
            JsonNode coordinateNode = coordinateArrayNode.get(i);
            double x = coordinateNode.get(0).asDouble();
            double y = coordinateNode.get(1).asDouble();
            coordinates[i] = new Coordinate(x, y);
        }
        return coordinates;
    }
}
