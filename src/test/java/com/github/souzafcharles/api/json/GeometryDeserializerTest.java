package com.github.souzafcharles.api.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GeometryDeserializerTest {

    private final GeometryDeserializer deserializer = new GeometryDeserializer();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ------------------------------------------------------------
    // TEST: Deserialize Point
    // ------------------------------------------------------------
    @Test
    void deserialize_WhenValidPoint_ShouldReturnPoint() throws IOException {
        // Arrange
        String json = "{\"type\":\"Point\",\"coordinates\":[10.5,20.5]}";
        JsonParser parser = objectMapper.createParser(json);

        // Act
        Geometry result = deserializer.deserialize(parser, null);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Point);
        Point point = (Point) result;
        assertEquals(10.5, point.getX(), 0.001);
        assertEquals(20.5, point.getY(), 0.001);
    }

    @Test
    void deserialize_WhenPointWithNullCoordinates_ShouldReturnNull() throws IOException {
        // Arrange
        String json = "{\"type\":\"Point\",\"coordinates\":null}";
        JsonParser parser = objectMapper.createParser(json);

        // Act
        Geometry result = deserializer.deserialize(parser, null);

        // Assert
        assertNull(result);
    }

    @Test
    void deserialize_WhenPointWithEmptyCoordinates_ShouldReturnNull() throws IOException {
        // Arrange
        String json = "{\"type\":\"Point\",\"coordinates\":[]}";
        JsonParser parser = objectMapper.createParser(json);

        // Act
        Geometry result = deserializer.deserialize(parser, null);

        // Assert
        assertNull(result);
    }

    // ------------------------------------------------------------
    // TEST: Deserialize LineString
    // ------------------------------------------------------------
    @Test
    void deserialize_WhenValidLineString_ShouldReturnLineString() throws IOException {
        // Arrange
        String json = "{\"type\":\"LineString\",\"coordinates\":[[0,0],[10,10],[20,20]]}";
        JsonParser parser = objectMapper.createParser(json);

        // Act
        Geometry result = deserializer.deserialize(parser, null);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof LineString);
        LineString lineString = (LineString) result;
        assertEquals(3, lineString.getNumPoints());
        assertEquals(0.0, lineString.getCoordinateN(0).x, 0.001);
        assertEquals(0.0, lineString.getCoordinateN(0).y, 0.001);
    }

    @Test
    void deserialize_WhenLineStringWithNullCoordinates_ShouldReturnNull() throws IOException {
        // Arrange
        String json = "{\"type\":\"LineString\",\"coordinates\":null}";
        JsonParser parser = objectMapper.createParser(json);

        // Act
        Geometry result = deserializer.deserialize(parser, null);

        // Assert
        assertNull(result);
    }

    @Test
    void deserialize_WhenLineStringWithEmptyCoordinates_ShouldReturnEmptyLineString() throws IOException {
        // Arrange
        String json = "{\"type\":\"LineString\",\"coordinates\":[]}";
        JsonParser parser = objectMapper.createParser(json);

        // Act
        Geometry result = deserializer.deserialize(parser, null);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof LineString);
        LineString lineString = (LineString) result;
        assertEquals(0, lineString.getNumPoints());
    }

    // ------------------------------------------------------------
    // TEST: Deserialize Polygon
    // ------------------------------------------------------------
    @Test
    void deserialize_WhenValidPolygon_ShouldReturnPolygon() throws IOException {
        // Arrange
        String json = "{\"type\":\"Polygon\",\"coordinates\":[[[0,0],[0,10],[10,10],[10,0],[0,0]]]}";
        JsonParser parser = objectMapper.createParser(json);

        // Act
        Geometry result = deserializer.deserialize(parser, null);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Polygon);
        Polygon polygon = (Polygon) result;
        assertNotNull(polygon.getExteriorRing());
        assertEquals(5, polygon.getExteriorRing().getNumPoints());
    }

    @Test
    void deserialize_WhenPolygonWithNullCoordinates_ShouldReturnNull() throws IOException {
        // Arrange
        String json = "{\"type\":\"Polygon\",\"coordinates\":null}";
        JsonParser parser = objectMapper.createParser(json);

        // Act
        Geometry result = deserializer.deserialize(parser, null);

        // Assert
        assertNull(result);
    }

    @Test
    void deserialize_WhenPolygonWithEmptyCoordinates_ShouldReturnNull() throws IOException {
        // Arrange
        String json = "{\"type\":\"Polygon\",\"coordinates\":[]}";
        JsonParser parser = objectMapper.createParser(json);

        // Act
        Geometry result = deserializer.deserialize(parser, null);

        // Assert
        assertNull(result);
    }

    @Test
    void deserialize_WhenPolygonWithInvalidCoordinates_ShouldThrowException() throws IOException {
        // Arrange
        String json = "{\"type\":\"Polygon\",\"coordinates\":[[[0,0],[0,10],[10,10]]]}"; // Only 3 points
        JsonParser parser = objectMapper.createParser(json);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> deserializer.deserialize(parser, null));
    }

    // ------------------------------------------------------------
    // TEST: Unsupported Geometry Types
    // ------------------------------------------------------------
    @Test
    void deserialize_WhenUnsupportedGeometryType_ShouldThrowException() throws IOException {
        // Arrange
        String json = "{\"type\":\"MultiPoint\",\"coordinates\":[[1,2],[3,4]]}";
        JsonParser parser = objectMapper.createParser(json);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> deserializer.deserialize(parser, null));
    }

    @Test
    void deserialize_WhenMultiLineStringType_ShouldThrowException() throws IOException {
        // Arrange
        String json = "{\"type\":\"MultiLineString\",\"coordinates\":[[[0,0],[1,1]],[[2,2],[3,3]]]}";
        JsonParser parser = objectMapper.createParser(json);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> deserializer.deserialize(parser, null));
    }

    @Test
    void deserialize_WhenMultiPolygonType_ShouldThrowException() throws IOException {
        // Arrange
        String json = "{\"type\":\"MultiPolygon\",\"coordinates\":[[[[0,0],[0,1],[1,1],[1,0],[0,0]]]]}";
        JsonParser parser = objectMapper.createParser(json);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> deserializer.deserialize(parser, null));
    }

    // ------------------------------------------------------------
    // TEST: Missing Type Field
    // ------------------------------------------------------------
    @Test
    void deserialize_WhenMissingTypeField_ShouldThrowException() throws IOException {
        // Arrange
        String json = "{\"coordinates\":[10,20]}";
        JsonParser parser = objectMapper.createParser(json);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> deserializer.deserialize(parser, null));
    }

    // ------------------------------------------------------------
    // TEST: Coordinate Parsing
    // ------------------------------------------------------------
    @Test
    void deserialize_WhenPointWithDecimalCoordinates_ShouldHandleDecimals() throws IOException {
        // Arrange
        String json = "{\"type\":\"Point\",\"coordinates\":[123.456,789.012]}";
        JsonParser parser = objectMapper.createParser(json);

        // Act
        Geometry result = deserializer.deserialize(parser, null);

        // Assert
        assertNotNull(result);
        Point point = (Point) result;
        assertEquals(123.456, point.getX(), 0.001);
        assertEquals(789.012, point.getY(), 0.001);
    }

    // ------------------------------------------------------------
    // TEST: Simple LineString (2 points)
    // ------------------------------------------------------------
    @Test
    void deserialize_WhenLineStringWithTwoPoints_ShouldReturnLineString() throws IOException {
        // Arrange
        String json = "{\"type\":\"LineString\",\"coordinates\":[[1,2],[3,4]]}";
        JsonParser parser = objectMapper.createParser(json);

        // Act
        Geometry result = deserializer.deserialize(parser, null);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof LineString);
        LineString lineString = (LineString) result;
        assertEquals(2, lineString.getNumPoints());
        assertEquals(1.0, lineString.getCoordinateN(0).x, 0.001);
        assertEquals(2.0, lineString.getCoordinateN(0).y, 0.001);
    }

    // ------------------------------------------------------------
    // TEST: Null JSON
    // ------------------------------------------------------------
    @Test
    void deserialize_WhenJsonIsNull_ShouldReturnNull() throws IOException {
        // Arrange
        String json = "null";
        JsonParser parser = objectMapper.createParser(json);

        // Act
        Geometry result = deserializer.deserialize(parser, null);

        // Assert
        assertNull(result);
    }
}