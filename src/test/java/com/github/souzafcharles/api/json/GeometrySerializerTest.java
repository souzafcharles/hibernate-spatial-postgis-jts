package com.github.souzafcharles.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.*;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeometrySerializerTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    private final GeometrySerializer serializer = new GeometrySerializer();
    private final GeometryFactory geometryFactory = new GeometryFactory();

    // ------------------------------------------------------------
    // TEST: Serialize Null Geometry
    // ------------------------------------------------------------
    @Test
    void serialize_WhenGeometryIsNull_ShouldWriteNull() throws IOException {
        // Arrange
        Geometry geometry = null;

        // Act
        serializer.serialize(geometry, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNull();
        verify(jsonGenerator, never()).writeStartObject();
    }

    // ------------------------------------------------------------
    // TEST: Serialize Point
    // ------------------------------------------------------------
    @Test
    void serialize_WhenGeometryIsPoint_ShouldWritePoint() throws IOException {
        // Arrange
        Point point = geometryFactory.createPoint(new Coordinate(10.0, 20.0));

        // Act
        serializer.serialize(point, jsonGenerator, serializerProvider);

        // Assert
        InOrder inOrder = inOrder(jsonGenerator);
        inOrder.verify(jsonGenerator).writeStartObject();
        inOrder.verify(jsonGenerator).writeStringField("type", "Point");
        inOrder.verify(jsonGenerator).writeArrayFieldStart("coordinates");
        inOrder.verify(jsonGenerator).writeNumber(10.0);
        inOrder.verify(jsonGenerator).writeNumber(20.0);
        inOrder.verify(jsonGenerator).writeEndArray();
        inOrder.verify(jsonGenerator).writeEndObject();
    }

    // ------------------------------------------------------------
    // TEST: Serialize LineString
    // ------------------------------------------------------------
    @Test
    void serialize_WhenGeometryIsLineString_ShouldWriteLineString() throws IOException {
        // Arrange
        Coordinate[] coordinates = {
                new Coordinate(0.0, 0.0),
                new Coordinate(10.0, 10.0)
        };
        LineString lineString = geometryFactory.createLineString(coordinates);

        // Act
        serializer.serialize(lineString, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "LineString");
        verify(jsonGenerator).writeArrayFieldStart("coordinates");
        verify(jsonGenerator, atLeastOnce()).writeNumber(anyDouble());
        verify(jsonGenerator).writeEndObject();
    }

    // ------------------------------------------------------------
    // TEST: Serialize Polygon
    // ------------------------------------------------------------
    @Test
    void serialize_WhenGeometryIsPolygon_ShouldWritePolygon() throws IOException {
        // Arrange
        Coordinate[] shellCoordinates = {
                new Coordinate(0.0, 0.0),
                new Coordinate(0.0, 10.0),
                new Coordinate(10.0, 10.0),
                new Coordinate(10.0, 0.0),
                new Coordinate(0.0, 0.0)
        };
        Polygon polygon = geometryFactory.createPolygon(shellCoordinates);

        // Act
        serializer.serialize(polygon, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "Polygon");
        verify(jsonGenerator).writeArrayFieldStart("coordinates");
        verify(jsonGenerator, atLeastOnce()).writeNumber(anyDouble());
        verify(jsonGenerator).writeEndObject();
    }

    // ------------------------------------------------------------
    // TEST: Serialize Unsupported Geometry Type
    // ------------------------------------------------------------
    @Test
    void serialize_WhenGeometryIsUnsupportedType_ShouldWriteBasicInfo() throws IOException {
        // Arrange
        MultiPoint multiPoint = geometryFactory.createMultiPointFromCoords(
                new Coordinate[]{new Coordinate(1.0, 2.0), new Coordinate(3.0, 4.0)}
        );

        // Act
        serializer.serialize(multiPoint, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "MultiPoint");
        verify(jsonGenerator).writeStringField("geometryType", "MultiPoint");
        verify(jsonGenerator).writeEndObject();
        verify(jsonGenerator, never()).writeArrayFieldStart("coordinates");
    }

    // ------------------------------------------------------------
    // TEST: Point with Specific Coordinates
    // ------------------------------------------------------------
    @Test
    void serialize_WhenPointHasSpecificCoordinates_ShouldWriteCorrectValues() throws IOException {
        // Arrange
        Point point = geometryFactory.createPoint(new Coordinate(123.456, 789.012));

        // Act
        serializer.serialize(point, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeNumber(123.456);
        verify(jsonGenerator).writeNumber(789.012);
    }

    // ------------------------------------------------------------
    // TEST: Empty LineString
    // ------------------------------------------------------------
    @Test
    void serialize_WhenLineStringIsEmpty_ShouldHandleEmptyCoordinates() throws IOException {
        // Arrange
        LineString lineString = geometryFactory.createLineString();

        // Act
        serializer.serialize(lineString, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "LineString");
        verify(jsonGenerator).writeArrayFieldStart("coordinates");
        verify(jsonGenerator).writeEndObject();
    }

    // ------------------------------------------------------------
    // TEST: Two Coordinate LineString
    // ------------------------------------------------------------
    @Test
    void serialize_WhenLineStringHasTwoCoordinates_ShouldWriteCorrectly() throws IOException {
        // Arrange
        Coordinate[] coordinates = {
                new Coordinate(5.0, 6.0),
                new Coordinate(7.0, 8.0)
        };
        LineString lineString = geometryFactory.createLineString(coordinates);

        // Act
        serializer.serialize(lineString, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "LineString");
        verify(jsonGenerator).writeArrayFieldStart("coordinates");
        verify(jsonGenerator).writeEndObject();
    }

    // ------------------------------------------------------------
    // TEST: MultiLineString (Unsupported)
    // ------------------------------------------------------------
    @Test
    void serialize_WhenGeometryIsMultiLineString_ShouldWriteBasicInfo() throws IOException {
        // Arrange
        LineString[] lineStrings = {
                geometryFactory.createLineString(new Coordinate[]{new Coordinate(0,0), new Coordinate(1,1)}),
                geometryFactory.createLineString(new Coordinate[]{new Coordinate(2,2), new Coordinate(3,3)})
        };
        MultiLineString multiLineString = geometryFactory.createMultiLineString(lineStrings);

        // Act
        serializer.serialize(multiLineString, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "MultiLineString");
        verify(jsonGenerator).writeStringField("geometryType", "MultiLineString");
        verify(jsonGenerator).writeEndObject();
        verify(jsonGenerator, never()).writeArrayFieldStart("coordinates");
    }

    // ------------------------------------------------------------
    // TEST: Geometry Collection (Unsupported)
    // ------------------------------------------------------------
    @Test
    void serialize_WhenGeometryIsGeometryCollection_ShouldWriteBasicInfo() throws IOException {
        // Arrange
        Geometry[] geometries = {
                geometryFactory.createPoint(new Coordinate(1, 2)),
                geometryFactory.createLineString(new Coordinate[]{new Coordinate(0,0), new Coordinate(1,1)})
        };
        GeometryCollection geometryCollection = geometryFactory.createGeometryCollection(geometries);

        // Act
        serializer.serialize(geometryCollection, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "GeometryCollection");
        verify(jsonGenerator).writeStringField("geometryType", "GeometryCollection");
        verify(jsonGenerator).writeEndObject();
    }

    // ------------------------------------------------------------
    // TEST: MultiPolygon (Unsupported)
    // ------------------------------------------------------------
    @Test
    void serialize_WhenGeometryIsMultiPolygon_ShouldWriteBasicInfo() throws IOException {
        // Arrange
        Polygon[] polygons = {
                geometryFactory.createPolygon(new Coordinate[]{
                        new Coordinate(0,0), new Coordinate(0,1), new Coordinate(1,1), new Coordinate(1,0), new Coordinate(0,0)
                })
        };
        MultiPolygon multiPolygon = geometryFactory.createMultiPolygon(polygons);

        // Act
        serializer.serialize(multiPolygon, jsonGenerator, serializerProvider);

        // Assert
        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeStringField("type", "MultiPolygon");
        verify(jsonGenerator).writeStringField("geometryType", "MultiPolygon");
        verify(jsonGenerator).writeEndObject();
    }
}