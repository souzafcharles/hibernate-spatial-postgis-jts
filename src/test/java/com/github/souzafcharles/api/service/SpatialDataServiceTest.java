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
}