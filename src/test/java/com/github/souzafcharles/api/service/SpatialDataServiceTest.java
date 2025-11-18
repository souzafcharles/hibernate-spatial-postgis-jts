package com.github.souzafcharles.api.service;

import com.github.souzafcharles.api.model.dto.*;
import com.github.souzafcharles.api.model.entity.SpatialData;
import com.github.souzafcharles.api.repository.SpatialDataRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.locationtech.jts.geom.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpatialDataServiceTest {

    @Mock
    private SpatialDataRepository spatialDataRepository;

    @InjectMocks
    private SpatialDataService spatialDataService;

    // Test Data
    private final List<Double> pointCoordinates = Arrays.asList(1.0, 2.0);
    private final List<List<Double>> multiPointCoordinates = Arrays.asList(
            Arrays.asList(1.0, 2.0),
            Arrays.asList(3.0, 4.0)
    );
    private final List<List<Double>> lineStringCoordinates = Arrays.asList(
            Arrays.asList(1.0, 2.0),
            Arrays.asList(3.0, 4.0)
    );
    private final List<List<List<Double>>> multiLineStringCoordinates = Arrays.asList(
            Arrays.asList(
                    Arrays.asList(1.0, 2.0),
                    Arrays.asList(3.0, 4.0)
            )
    );
    private final List<List<List<Double>>> polygonCoordinates = Arrays.asList(
            Arrays.asList(
                    Arrays.asList(0.0, 0.0),
                    Arrays.asList(0.0, 1.0),
                    Arrays.asList(1.0, 1.0),
                    Arrays.asList(1.0, 0.0),
                    Arrays.asList(0.0, 0.0)
            )
    );
    private final List<List<List<List<Double>>>> multiPolygonCoordinates = Arrays.asList(
            Arrays.asList(
                    Arrays.asList(
                            Arrays.asList(0.0, 0.0),
                            Arrays.asList(0.0, 1.0),
                            Arrays.asList(1.0, 1.0),
                            Arrays.asList(1.0, 0.0),
                            Arrays.asList(0.0, 0.0)
                    )
            )
    );

    // ------------------------------------------------------------
    // CREATE – From Serializer Format
    // ------------------------------------------------------------
    @Test
    void createFromSerializerFormat_WithValidData_ShouldSaveAndReturnResponse() {
        // Arrange
        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                pointCoordinates, multiPointCoordinates, lineStringCoordinates,
                multiLineStringCoordinates, polygonCoordinates, multiPolygonCoordinates
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromSerializerFormat_WithNullPoint_ShouldNotCreatePoint() {
        // Arrange
        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, multiPointCoordinates, lineStringCoordinates,
                multiLineStringCoordinates, polygonCoordinates, multiPolygonCoordinates
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromSerializerFormat_WithAllNull_ShouldSaveEmptyEntity() {
        // Arrange
        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, null, null, null, null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    // ------------------------------------------------------------
    // CREATE – From Deserializer Format
    // ------------------------------------------------------------
    @Test
    void createFromDeserializerFormat_WithValidData_ShouldSaveAndReturnResponse() {
        // Arrange
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(1.0, 2.0));

        SpatialDataDeserializerRequestDTO request = new SpatialDataDeserializerRequestDTO(
                point, null, null, null, null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromDeserializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromDeserializerFormat_WithAllNull_ShouldSaveEmptyEntity() {
        // Arrange
        SpatialDataDeserializerRequestDTO request = new SpatialDataDeserializerRequestDTO(
                null, null, null, null, null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromDeserializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    // ------------------------------------------------------------
    // READ – List all
    // ------------------------------------------------------------
    @Test
    void getAllSpatialData_WhenDataExists_ShouldReturnList() {
        // Arrange
        SpatialData spatialData1 = new SpatialData();
        spatialData1.setId(1L);

        SpatialData spatialData2 = new SpatialData();
        spatialData2.setId(2L);

        List<SpatialData> spatialDataList = Arrays.asList(spatialData1, spatialData2);

        when(spatialDataRepository.findAll()).thenReturn(spatialDataList);

        // Act
        List<SpatialDataResponseDTO> result = spatialDataService.getAllSpatialData();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(spatialDataRepository, times(1)).findAll();
    }

    @Test
    void getAllSpatialData_WhenNoData_ShouldReturnEmptyList() {
        // Arrange
        when(spatialDataRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<SpatialDataResponseDTO> result = spatialDataService.getAllSpatialData();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(spatialDataRepository, times(1)).findAll();
    }

    // ------------------------------------------------------------
    // READ – Find by ID
    // ------------------------------------------------------------
    @Test
    void getById_WhenExists_ShouldReturnSpatialData() {
        // Arrange
        Long spatialDataId = 1L;
        SpatialData spatialData = new SpatialData();
        spatialData.setId(spatialDataId);

        when(spatialDataRepository.findById(spatialDataId)).thenReturn(Optional.of(spatialData));

        // Act
        SpatialDataResponseDTO result = spatialDataService.getById(spatialDataId);

        // Assert
        assertNotNull(result);
        assertEquals(spatialDataId, result.id());
        verify(spatialDataRepository, times(1)).findById(spatialDataId);
    }

    @Test
    void getById_WhenNotExists_ShouldThrowException() {
        // Arrange
        Long spatialDataId = 999L;
        when(spatialDataRepository.findById(spatialDataId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> spatialDataService.getById(spatialDataId));

        assertTrue(exception.getMessage().contains(String.valueOf(spatialDataId)));
        verify(spatialDataRepository, times(1)).findById(spatialDataId);
    }

    // ------------------------------------------------------------
    // READ – GeoJSON (Polygon only)
    // ------------------------------------------------------------
    @Test
    void getPolygonAsGeoJson_WhenPolygonExists_ShouldReturnGeoJson() {
        // Arrange
        Long spatialDataId = 1L;
        SpatialData spatialData = new SpatialData();
        spatialData.setId(spatialDataId);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] coordinates = {
                new Coordinate(0.0, 0.0),
                new Coordinate(0.0, 1.0),
                new Coordinate(1.0, 1.0),
                new Coordinate(1.0, 0.0),
                new Coordinate(0.0, 0.0)
        };
        Polygon polygon = geometryFactory.createPolygon(coordinates);
        spatialData.setPolygon(polygon);

        when(spatialDataRepository.findById(spatialDataId)).thenReturn(Optional.of(spatialData));

        // Act
        GeoJsonResponseDTO result = spatialDataService.getPolygonAsGeoJson(spatialDataId);

        // Assert
        assertNotNull(result);
        assertEquals("Feature", result.type());
        assertNotNull(result.geometry());
        assertNotNull(result.properties());
        verify(spatialDataRepository, times(1)).findById(spatialDataId);
    }

    @Test
    void getPolygonAsGeoJson_WhenPolygonNotExists_ShouldThrowException() {
        // Arrange
        Long spatialDataId = 1L;
        SpatialData spatialData = new SpatialData();
        spatialData.setId(spatialDataId);
        // No polygon set

        when(spatialDataRepository.findById(spatialDataId)).thenReturn(Optional.of(spatialData));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> spatialDataService.getPolygonAsGeoJson(spatialDataId));

        assertTrue(exception.getMessage().contains(String.valueOf(spatialDataId)));
        verify(spatialDataRepository, times(1)).findById(spatialDataId);
    }

    @Test
    void getPolygonAsGeoJson_WhenSpatialDataNotExists_ShouldThrowException() {
        // Arrange
        Long spatialDataId = 999L;
        when(spatialDataRepository.findById(spatialDataId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> spatialDataService.getPolygonAsGeoJson(spatialDataId));

        assertTrue(exception.getMessage().contains(String.valueOf(spatialDataId)));
        verify(spatialDataRepository, times(1)).findById(spatialDataId);
    }

    // ------------------------------------------------------------
    // Edge Cases and Additional Scenarios
    // ------------------------------------------------------------
    @Test
    void createFromSerializerFormat_WithInvalidPolygonCoordinates_ShouldThrowException() {
        // Arrange
        List<List<List<Double>>> invalidPolygonCoordinates = Arrays.asList(
                Arrays.asList(
                        Arrays.asList(0.0, 0.0),
                        Arrays.asList(0.0, 1.0),
                        Arrays.asList(1.0, 1.0)
                        // Missing closing coordinate - less than 4 points
                )
        );

        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, null, null, null, invalidPolygonCoordinates, null
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> spatialDataService.createFromSerializerFormat(request));
    }

    @Test
    void createFromSerializerFormat_WithEmptyCollections_ShouldSaveSuccessfully() {
        // Arrange
        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                pointCoordinates,
                Arrays.asList(), // empty multipoint
                Arrays.asList(), // empty linestring
                Arrays.asList(), // empty multilinestring
                Arrays.asList(), // empty polygon
                Arrays.asList()  // empty multipolygon
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    // ------------------------------------------------------------
    // CREATE – From Serializer Format - Additional Tests
    // ------------------------------------------------------------
    @Test
    void createFromSerializerFormat_WithOnlyPoint_ShouldCreateOnlyPoint() {
        // Arrange
        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                pointCoordinates, null, null, null, null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromSerializerFormat_WithOnlyLineString_ShouldCreateOnlyLineString() {
        // Arrange
        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, null, lineStringCoordinates, null, null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromSerializerFormat_WithOnlyMultiPoint_ShouldCreateOnlyMultiPoint() {
        // Arrange
        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, multiPointCoordinates, null, null, null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromSerializerFormat_WithOnlyMultiLineString_ShouldCreateOnlyMultiLineString() {
        // Arrange
        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, null, null, multiLineStringCoordinates, null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromSerializerFormat_WithOnlyPolygon_ShouldCreateOnlyPolygon() {
        // Arrange
        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, null, null, null, polygonCoordinates, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromSerializerFormat_WithOnlyMultiPolygon_ShouldCreateOnlyMultiPolygon() {
        // Arrange
        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, null, null, null, null, multiPolygonCoordinates
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    // ------------------------------------------------------------
    // CREATE – From Deserializer Format - Additional Tests
    // ------------------------------------------------------------
    @Test
    void createFromDeserializerFormat_WithAllGeometryTypes_ShouldSaveAll() {
        // Arrange
        GeometryFactory geometryFactory = new GeometryFactory();

        Point point = geometryFactory.createPoint(new Coordinate(1.0, 2.0));
        MultiPoint multiPoint = geometryFactory.createMultiPointFromCoords(
                new Coordinate[]{new Coordinate(1, 2), new Coordinate(3, 4)}
        );
        LineString lineString = geometryFactory.createLineString(
                new Coordinate[]{new Coordinate(0, 0), new Coordinate(1, 1)}
        );
        MultiLineString multiLineString = geometryFactory.createMultiLineString(new LineString[]{
                geometryFactory.createLineString(new Coordinate[]{new Coordinate(0,0), new Coordinate(1,1)})
        });
        Polygon polygon = geometryFactory.createPolygon(new Coordinate[]{
                new Coordinate(0,0), new Coordinate(0,1), new Coordinate(1,1), new Coordinate(1,0), new Coordinate(0,0)
        });
        MultiPolygon multiPolygon = geometryFactory.createMultiPolygon(new Polygon[]{
                geometryFactory.createPolygon(new Coordinate[]{
                        new Coordinate(0,0), new Coordinate(0,1), new Coordinate(1,1), new Coordinate(1,0), new Coordinate(0,0)
                })
        });

        SpatialDataDeserializerRequestDTO request = new SpatialDataDeserializerRequestDTO(
                point, multiPoint, lineString, multiLineString, polygon, multiPolygon
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromDeserializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromDeserializerFormat_WithOnlyMultiPoint_ShouldCreateOnlyMultiPoint() {
        // Arrange
        GeometryFactory geometryFactory = new GeometryFactory();
        MultiPoint multiPoint = geometryFactory.createMultiPointFromCoords(
                new Coordinate[]{new Coordinate(1, 2), new Coordinate(3, 4)}
        );

        SpatialDataDeserializerRequestDTO request = new SpatialDataDeserializerRequestDTO(
                null, multiPoint, null, null, null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromDeserializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromDeserializerFormat_WithOnlyLineString_ShouldCreateOnlyLineString() {
        // Arrange
        GeometryFactory geometryFactory = new GeometryFactory();
        LineString lineString = geometryFactory.createLineString(
                new Coordinate[]{new Coordinate(0, 0), new Coordinate(1, 1)}
        );

        SpatialDataDeserializerRequestDTO request = new SpatialDataDeserializerRequestDTO(
                null, null, lineString, null, null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromDeserializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromDeserializerFormat_WithOnlyMultiLineString_ShouldCreateOnlyMultiLineString() {
        // Arrange
        GeometryFactory geometryFactory = new GeometryFactory();
        MultiLineString multiLineString = geometryFactory.createMultiLineString(new LineString[]{
                geometryFactory.createLineString(new Coordinate[]{new Coordinate(0,0), new Coordinate(1,1)})
        });

        SpatialDataDeserializerRequestDTO request = new SpatialDataDeserializerRequestDTO(
                null, null, null, multiLineString, null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromDeserializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromDeserializerFormat_WithOnlyPolygon_ShouldCreateOnlyPolygon() {
        // Arrange
        GeometryFactory geometryFactory = new GeometryFactory();
        Polygon polygon = geometryFactory.createPolygon(new Coordinate[]{
                new Coordinate(0,0), new Coordinate(0,1), new Coordinate(1,1), new Coordinate(1,0), new Coordinate(0,0)
        });

        SpatialDataDeserializerRequestDTO request = new SpatialDataDeserializerRequestDTO(
                null, null, null, null, polygon, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromDeserializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromDeserializerFormat_WithOnlyMultiPolygon_ShouldCreateOnlyMultiPolygon() {
        // Arrange
        GeometryFactory geometryFactory = new GeometryFactory();
        MultiPolygon multiPolygon = geometryFactory.createMultiPolygon(new Polygon[]{
                geometryFactory.createPolygon(new Coordinate[]{
                        new Coordinate(0,0), new Coordinate(0,1), new Coordinate(1,1), new Coordinate(1,0), new Coordinate(0,0)
                })
        });

        SpatialDataDeserializerRequestDTO request = new SpatialDataDeserializerRequestDTO(
                null, null, null, null, null, multiPolygon
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromDeserializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    // ------------------------------------------------------------
    // READ – GeoJSON - Additional Tests for Different Geometry Types
    // ------------------------------------------------------------
    @Test
    void getPolygonAsGeoJson_WhenCalled_ShouldConvertPolygonToGeoJson() {
        // Arrange
        Long spatialDataId = 1L;
        SpatialData spatialData = new SpatialData();
        spatialData.setId(spatialDataId);

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] coordinates = {
                new Coordinate(0.0, 0.0),
                new Coordinate(0.0, 10.0),
                new Coordinate(10.0, 10.0),
                new Coordinate(10.0, 0.0),
                new Coordinate(0.0, 0.0)
        };
        Polygon polygon = geometryFactory.createPolygon(coordinates);
        spatialData.setPolygon(polygon);

        when(spatialDataRepository.findById(spatialDataId)).thenReturn(Optional.of(spatialData));

        // Act
        GeoJsonResponseDTO result = spatialDataService.getPolygonAsGeoJson(spatialDataId);

        // Assert
        assertNotNull(result);
        assertEquals("Feature", result.type());
        assertNotNull(result.geometry());
        assertNotNull(result.properties());
        verify(spatialDataRepository, times(1)).findById(spatialDataId);
    }

    // ------------------------------------------------------------
    // Edge Cases - Geometry Creation Validation
    // ------------------------------------------------------------
    @Test
    void createFromSerializerFormat_WithInvalidPointCoordinates_ShouldNotCreatePoint() {
        // Arrange
        List<Double> invalidPoint = Arrays.asList(1.0); // Only one coordinate

        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                invalidPoint, null, null, null, null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromSerializerFormat_WithEmptyMultiPoint_ShouldNotCreateMultiPoint() {
        // Arrange
        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, Arrays.asList(), null, null, null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromSerializerFormat_WithEmptyLineString_ShouldNotCreateLineString() {
        // Arrange
        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, null, Arrays.asList(), null, null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromSerializerFormat_WithEmptyMultiLineString_ShouldNotCreateMultiLineString() {
        // Arrange
        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, null, null, Arrays.asList(), null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromSerializerFormat_WithEmptyMultiPolygon_ShouldNotCreateMultiPolygon() {
        // Arrange
        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, null, null, null, null, Arrays.asList()
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    // ------------------------------------------------------------
    // Service Initialization and Constructor Tests
    // ------------------------------------------------------------
    @Test
    void spatialDataService_WhenInitialized_ShouldNotBeNull() {
        // Arrange & Act & Assert
        assertNotNull(spatialDataService);
    }

    @Test
    void spatialDataService_ShouldHaveRepositoryInjected() {
        // Arrange & Act & Assert
        assertNotNull(spatialDataService);
    }

    // ------------------------------------------------------------
    // Error Scenarios - Additional Exception Tests
    // ------------------------------------------------------------
    @Test
    void createFromSerializerFormat_WithNullRequest_ShouldThrowException() {
        // Arrange
        SpatialDataSerializerRequestDTO request = null;

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> spatialDataService.createFromSerializerFormat(request));
    }

    // ------------------------------------------------------------
    // Data Conversion Tests (Testing private methods indirectly)
    // ------------------------------------------------------------
    @Test
    void createFromSerializerFormat_WithValidMultiPolygon_ShouldCreateMultiPolygon() {
        // Arrange
        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, null, null, null, null, multiPolygonCoordinates
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromSerializerFormat_WithValidMultiLineString_ShouldCreateMultiLineString() {
        // Arrange
        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, null, null, multiLineStringCoordinates, null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    // ------------------------------------------------------------
    // Response Mapping Tests
    // ------------------------------------------------------------
    @Test
    void toResponse_ShouldMapAllFieldsCorrectly() {
        // Arrange
        SpatialData entity = new SpatialData();
        entity.setId(1L);

        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(1.0, 2.0));
        entity.setPoint(point);

        // Set up the repository mock BEFORE calling the service method
        when(spatialDataRepository.findById(1L)).thenReturn(Optional.of(entity));

        // Act
        SpatialDataResponseDTO result = spatialDataService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertNotNull(result.point());
        assertEquals(point, result.point());
        verify(spatialDataRepository, times(1)).findById(1L);
    }

    // ------------------------------------------------------------
    // Boundary Value Tests
    // ------------------------------------------------------------
    @Test
    void createFromSerializerFormat_WithMinimumValidPolygon_ShouldCreatePolygon() {
        // Arrange - Polygon with exactly 4 points (minimum for closed polygon)
        List<List<List<Double>>> minPolygonCoordinates = Arrays.asList(
                Arrays.asList(
                        Arrays.asList(0.0, 0.0),
                        Arrays.asList(0.0, 1.0),
                        Arrays.asList(1.0, 1.0),
                        Arrays.asList(0.0, 0.0) // Closing point
                )
        );

        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, null, null, null, minPolygonCoordinates, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    // ------------------------------------------------------------
    // Additional Edge Cases
    // ------------------------------------------------------------
    @Test
    void createFromSerializerFormat_WithValidMultiPointWithSinglePoint_ShouldCreateMultiPoint() {
        // Arrange
        List<List<Double>> singlePointMultiPoint = Arrays.asList(
                Arrays.asList(1.0, 2.0)
        );

        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, singlePointMultiPoint, null, null, null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromSerializerFormat_WithValidLineStringWithTwoPoints_ShouldCreateLineString() {
        // Arrange
        List<List<Double>> twoPointLineString = Arrays.asList(
                Arrays.asList(1.0, 2.0),
                Arrays.asList(3.0, 4.0)
        );

        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, null, twoPointLineString, null, null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    // Test for GeometryFactory initialization
    @Test
    void spatialDataService_WhenConstructed_ShouldInitializeGeometryFactory() {
        // Arrange & Act
        SpatialDataService service = new SpatialDataService(spatialDataRepository);

        // Assert
        assertNotNull(service);
    }

    // Test for GeoJson conversion with complex polygon
    @Test
    void getPolygonAsGeoJson_WithComplexPolygon_ShouldConvertCorrectly() {
        // Arrange
        Long spatialDataId = 1L;
        SpatialData spatialData = new SpatialData();
        spatialData.setId(spatialDataId);

        GeometryFactory geometryFactory = new GeometryFactory();
        // Create a more complex polygon
        Coordinate[] exteriorRing = {
                new Coordinate(0.0, 0.0),
                new Coordinate(0.0, 10.0),
                new Coordinate(10.0, 10.0),
                new Coordinate(10.0, 0.0),
                new Coordinate(0.0, 0.0)
        };

        Polygon polygon = geometryFactory.createPolygon(exteriorRing);
        spatialData.setPolygon(polygon);

        when(spatialDataRepository.findById(spatialDataId)).thenReturn(Optional.of(spatialData));

        // Act
        GeoJsonResponseDTO result = spatialDataService.getPolygonAsGeoJson(spatialDataId);

        // Assert
        assertNotNull(result);
        assertEquals("Feature", result.type());
        assertNotNull(result.geometry());
        assertNotNull(result.properties());
        verify(spatialDataRepository, times(1)).findById(spatialDataId);
    }

// ------------------------------------------------------------
// GeoJSON Conversion Tests - Testing Private Methods
// ------------------------------------------------------------

    @Test
    void getPolygonAsGeoJson_WithPolygonWithHoles_ShouldConvertCorrectly() {
        // Arrange
        Long spatialDataId = 1L;
        SpatialData spatialData = new SpatialData();
        spatialData.setId(spatialDataId);

        GeometryFactory geometryFactory = new GeometryFactory();

        // Create polygon with hole
        Coordinate[] exteriorRing = {
                new Coordinate(0.0, 0.0),
                new Coordinate(0.0, 10.0),
                new Coordinate(10.0, 10.0),
                new Coordinate(10.0, 0.0),
                new Coordinate(0.0, 0.0)
        };
        Coordinate[] interiorRing = {
                new Coordinate(2.0, 2.0),
                new Coordinate(2.0, 8.0),
                new Coordinate(8.0, 8.0),
                new Coordinate(8.0, 2.0),
                new Coordinate(2.0, 2.0)
        };

        Polygon polygon = geometryFactory.createPolygon(
                geometryFactory.createLinearRing(exteriorRing),
                new LinearRing[]{geometryFactory.createLinearRing(interiorRing)}
        );
        spatialData.setPolygon(polygon);

        when(spatialDataRepository.findById(spatialDataId)).thenReturn(Optional.of(spatialData));

        // Act
        GeoJsonResponseDTO result = spatialDataService.getPolygonAsGeoJson(spatialDataId);

        // Assert
        assertNotNull(result);
        assertEquals("Feature", result.type());
        assertNotNull(result.geometry());
        assertNotNull(result.properties());
        verify(spatialDataRepository, times(1)).findById(spatialDataId);
    }

    @Test
    void getPolygonAsGeoJson_WithEmptyGeometry_ShouldHandleGracefully() {
        // Arrange
        Long spatialDataId = 1L;
        SpatialData spatialData = new SpatialData();
        spatialData.setId(spatialDataId);
        // No geometries set - all null

        when(spatialDataRepository.findById(spatialDataId)).thenReturn(Optional.of(spatialData));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> spatialDataService.getPolygonAsGeoJson(spatialDataId));

        assertTrue(exception.getMessage().contains(String.valueOf(spatialDataId)));
        verify(spatialDataRepository, times(1)).findById(spatialDataId);
    }

    // Test edge cases for coordinate arrays
    @Test
    void getPolygonAsGeoJson_WithSinglePointPolygon_ShouldConvertCorrectly() {
        // Arrange
        Long spatialDataId = 1L;
        SpatialData spatialData = new SpatialData();
        spatialData.setId(spatialDataId);

        GeometryFactory geometryFactory = new GeometryFactory();
        // Technically invalid polygon but testing edge case
        Coordinate[] singlePoint = {
                new Coordinate(1.0, 1.0),
                new Coordinate(1.0, 1.0),
                new Coordinate(1.0, 1.0),
                new Coordinate(1.0, 1.0)
        };
        Polygon polygon = geometryFactory.createPolygon(singlePoint);
        spatialData.setPolygon(polygon);

        when(spatialDataRepository.findById(spatialDataId)).thenReturn(Optional.of(spatialData));

        // Act
        GeoJsonResponseDTO result = spatialDataService.getPolygonAsGeoJson(spatialDataId);

        // Assert
        assertNotNull(result);
        assertEquals("Feature", result.type());
        assertNotNull(result.geometry());
        assertNotNull(result.properties());
        verify(spatialDataRepository, times(1)).findById(spatialDataId);
    }

// ------------------------------------------------------------
// Tests for Geometry Validation Methods (Private Methods)
// ------------------------------------------------------------

    @Test
    void createFromSerializerFormat_WithVariousCoordinateValidations_ShouldHandleCorrectly() {
        // Test various coordinate validation scenarios

        // Test 1: Valid point coordinates
        SpatialDataSerializerRequestDTO validPointRequest = new SpatialDataSerializerRequestDTO(
                Arrays.asList(1.0, 2.0), null, null, null, null, null
        );

        // Test 2: Invalid point coordinates (null)
        SpatialDataSerializerRequestDTO nullPointRequest = new SpatialDataSerializerRequestDTO(
                null, null, null, null, null, null
        );

        // Test 3: Empty collections
        SpatialDataSerializerRequestDTO emptyCollectionsRequest = new SpatialDataSerializerRequestDTO(
                null, Arrays.asList(), Arrays.asList(), Arrays.asList(), Arrays.asList(), Arrays.asList()
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act & Assert - All should save successfully
        SpatialDataResponseDTO result1 = spatialDataService.createFromSerializerFormat(validPointRequest);
        assertNotNull(result1);

        SpatialDataResponseDTO result2 = spatialDataService.createFromSerializerFormat(nullPointRequest);
        assertNotNull(result2);

        SpatialDataResponseDTO result3 = spatialDataService.createFromSerializerFormat(emptyCollectionsRequest);
        assertNotNull(result3);

        verify(spatialDataRepository, times(3)).save(any(SpatialData.class));
    }

// ------------------------------------------------------------
// Tests for Geometry Creation Methods (Private Methods)
// ------------------------------------------------------------

    @Test
    void createFromSerializerFormat_WithComplexMultiPolygon_ShouldCreateSuccessfully() {
        // Arrange
        List<List<List<List<Double>>>> complexMultiPolygon = Arrays.asList(
                Arrays.asList( // First polygon
                        Arrays.asList( // Exterior ring
                                Arrays.asList(0.0, 0.0),
                                Arrays.asList(0.0, 5.0),
                                Arrays.asList(5.0, 5.0),
                                Arrays.asList(5.0, 0.0),
                                Arrays.asList(0.0, 0.0)
                        )
                ),
                Arrays.asList( // Second polygon
                        Arrays.asList( // Exterior ring
                                Arrays.asList(6.0, 6.0),
                                Arrays.asList(6.0, 8.0),
                                Arrays.asList(8.0, 8.0),
                                Arrays.asList(8.0, 6.0),
                                Arrays.asList(6.0, 6.0)
                        )
                )
        );

        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, null, null, null, null, complexMultiPolygon
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

    @Test
    void createFromSerializerFormat_WithComplexMultiLineString_ShouldCreateSuccessfully() {
        // Arrange
        List<List<List<Double>>> complexMultiLineString = Arrays.asList(
                Arrays.asList( // First line string
                        Arrays.asList(0.0, 0.0),
                        Arrays.asList(1.0, 1.0),
                        Arrays.asList(2.0, 0.0)
                ),
                Arrays.asList( // Second line string
                        Arrays.asList(3.0, 3.0),
                        Arrays.asList(4.0, 4.0),
                        Arrays.asList(5.0, 3.0)
                )
        );

        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, null, null, complexMultiLineString, null, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

// ------------------------------------------------------------
// Tests for Edge Cases in Geometry Creation
// ------------------------------------------------------------

    @Test
    void createFromSerializerFormat_WithLargeCoordinateArrays_ShouldHandleCorrectly() {
        // Arrange - Create a polygon with many points
        List<List<Double>> manyPoints = Arrays.asList(
                Arrays.asList(0.0, 0.0),
                Arrays.asList(0.0, 1.0),
                Arrays.asList(0.1, 1.0),
                Arrays.asList(0.2, 1.0),
                Arrays.asList(0.3, 1.0),
                Arrays.asList(0.4, 1.0),
                Arrays.asList(0.5, 1.0),
                Arrays.asList(1.0, 1.0),
                Arrays.asList(1.0, 0.0),
                Arrays.asList(0.0, 0.0)
        );

        List<List<List<Double>>> polygonWithManyPoints = Arrays.asList(manyPoints);

        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, null, null, null, polygonWithManyPoints, null
        );

        SpatialData savedEntity = new SpatialData();
        savedEntity.setId(1L);

        when(spatialDataRepository.save(any(SpatialData.class))).thenReturn(savedEntity);

        // Act
        SpatialDataResponseDTO result = spatialDataService.createFromSerializerFormat(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(spatialDataRepository, times(1)).save(any(SpatialData.class));
    }

// ------------------------------------------------------------
// Tests for Error Conditions
// ------------------------------------------------------------

    @Test
    void createFromSerializerFormat_WithMalformedPolygon_ShouldThrowException() {
        // Arrange - Polygon with only 2 points (invalid)
        List<List<List<Double>>> malformedPolygon = Arrays.asList(
                Arrays.asList(
                        Arrays.asList(0.0, 0.0),
                        Arrays.asList(1.0, 1.0)
                        // Missing closing point and not enough points
                )
        );

        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                null, null, null, null, malformedPolygon, null
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> spatialDataService.createFromSerializerFormat(request));
    }
}