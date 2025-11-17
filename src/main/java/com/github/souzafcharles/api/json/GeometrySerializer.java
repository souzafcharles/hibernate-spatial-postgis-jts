package com.github.souzafcharles.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Coordinate;

import java.io.IOException;

public class GeometrySerializer extends JsonSerializer<Geometry> {

    @Override
    public void serialize(Geometry geometry, JsonGenerator json, SerializerProvider serializers) throws IOException {
        if (geometry == null) {
            json.writeNull();
            return;
        }
        json.writeStartObject();
        json.writeStringField("type", geometry.getGeometryType());
        if (geometry instanceof Point point) {
            writePoint(point, json);
        } else if (geometry instanceof Polygon polygon) {
            writePolygon(polygon, json);
        } else if (geometry instanceof LineString lineString) {
            writeLineString(lineString, json);
        } else {
            // Unsupported geometry types fallback
            json.writeStringField("geometryType", geometry.getGeometryType());
        }
        json.writeEndObject();
    }

    private void writePoint(Point point, JsonGenerator json) throws IOException {
        json.writeArrayFieldStart("coordinates");
        json.writeNumber(point.getX());
        json.writeNumber(point.getY());
        json.writeEndArray();
    }

    private void writePolygon(Polygon polygon, JsonGenerator json) throws IOException {
        json.writeArrayFieldStart("coordinates");
        // Exterior ring (shell)
        json.writeStartArray();
        Coordinate[] exteriorCoordinates = polygon.getExteriorRing().getCoordinates();
        for (Coordinate coordinate : exteriorCoordinates) {
            json.writeStartArray();
            json.writeNumber(coordinate.getX());
            json.writeNumber(coordinate.getY());
            json.writeEndArray();
        }
        json.writeEndArray(); // end exterior ring
        json.writeEndArray(); // end coordinates
    }

    private void writeLineString(LineString lineString, JsonGenerator json) throws IOException {
        json.writeArrayFieldStart("coordinates");
        for (Coordinate coordinate : lineString.getCoordinates()) {
            json.writeStartArray();
            json.writeNumber(coordinate.getX());
            json.writeNumber(coordinate.getY());
            json.writeEndArray();
        }
        json.writeEndArray();
    }
}
