package com.github.souzafcharles.api.controller;

import com.github.souzafcharles.api.model.dto.*;
import com.github.souzafcharles.api.service.SpatialDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpatialDataControllerTest {

    @Mock
    private SpatialDataService spatialDataService;

    @InjectMocks
    private SpatialDataController spatialDataController;

    // Test Data
    private final List<Double> pointCoordinates = Arrays.asList(1.0, 2.0);
    private final List<List<Double>> multiPointCoordinates = Arrays.asList(
            Arrays.asList(1.0, 2.0),
            Arrays.asList(3.0, 4.0)
    );

    // ------------------------------------------------------------
    // CREATE – From Serializer
    // ------------------------------------------------------------
    @Test
    void createFromSerializer_WithValidRequest_ShouldReturnOk() {
        // Arrange
        SpatialDataSerializerRequestDTO request = new SpatialDataSerializerRequestDTO(
                pointCoordinates, multiPointCoordinates, null, null, null, null
        );

        SpatialDataResponseDTO serviceResponse = new SpatialDataResponseDTO(
                1L, null, null, null, null, null, null
        );

        when(spatialDataService.createFromSerializerFormat(request)).thenReturn(serviceResponse);

        // Act
        ResponseEntity<SpatialDataResponseDTO> result = spatialDataController.createFromSerializer(request);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(serviceResponse, result.getBody());
        verify(spatialDataService, times(1)).createFromSerializerFormat(request);
    }

    // ------------------------------------------------------------
    // CREATE – From Deserializer
    // ------------------------------------------------------------
    @Test
    void createFromDeserializer_WithValidRequest_ShouldReturnOk() {
        // Arrange
        SpatialDataDeserializerRequestDTO request = new SpatialDataDeserializerRequestDTO(
                null, null, null, null, null, null
        );

        SpatialDataResponseDTO serviceResponse = new SpatialDataResponseDTO(
                1L, null, null, null, null, null, null
        );

        when(spatialDataService.createFromDeserializerFormat(request)).thenReturn(serviceResponse);

        // Act
        ResponseEntity<SpatialDataResponseDTO> result = spatialDataController.createFromDeserializer(request);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(serviceResponse, result.getBody());
        verify(spatialDataService, times(1)).createFromDeserializerFormat(request);
    }

    // ------------------------------------------------------------
    // GET ALL
    // ------------------------------------------------------------
    @Test
    void getAll_WhenDataExists_ShouldReturnList() {
        // Arrange
        SpatialDataResponseDTO response1 = new SpatialDataResponseDTO(1L, null, null, null, null, null, null);
        SpatialDataResponseDTO response2 = new SpatialDataResponseDTO(2L, null, null, null, null, null, null);
        List<SpatialDataResponseDTO> serviceResponse = Arrays.asList(response1, response2);

        when(spatialDataService.getAllSpatialData()).thenReturn(serviceResponse);

        // Act
        ResponseEntity<List<SpatialDataResponseDTO>> result = spatialDataController.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(serviceResponse, result.getBody());
        assertEquals(2, result.getBody().size());
        verify(spatialDataService, times(1)).getAllSpatialData();
    }

    @Test
    void getAll_WhenNoData_ShouldReturnEmptyList() {
        // Arrange
        List<SpatialDataResponseDTO> serviceResponse = Arrays.asList();
        when(spatialDataService.getAllSpatialData()).thenReturn(serviceResponse);

        // Act
        ResponseEntity<List<SpatialDataResponseDTO>> result = spatialDataController.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
        verify(spatialDataService, times(1)).getAllSpatialData();
    }

    // ------------------------------------------------------------
    // GET BY ID
    // ------------------------------------------------------------
    @Test
    void getById_WhenExists_ShouldReturnSpatialData() {
        // Arrange
        Long spatialDataId = 1L;
        SpatialDataResponseDTO serviceResponse = new SpatialDataResponseDTO(
                spatialDataId, null, null, null, null, null, null
        );

        when(spatialDataService.getById(spatialDataId)).thenReturn(serviceResponse);

        // Act
        ResponseEntity<SpatialDataResponseDTO> result = spatialDataController.getById(spatialDataId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(serviceResponse, result.getBody());
        assertEquals(spatialDataId, result.getBody().id());
        verify(spatialDataService, times(1)).getById(spatialDataId);
    }

    // ------------------------------------------------------------
    // GET GEOJSON
    // ------------------------------------------------------------
    @Test
    void getAsGeoJson_WhenExists_ShouldReturnGeoJson() {
        // Arrange
        Long spatialDataId = 1L;

        Object mockGeometry = new Object() {
            public final String type = "Polygon";
            public final double[][][] coordinates = {{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {1.0, 2.0}}};
        };

        Object mockProperties = new Object() {
            public final String description = "Polygon from SpatialData";
            public final Long id = spatialDataId;
        };

        GeoJsonResponseDTO serviceResponse = new GeoJsonResponseDTO(
                "Feature",
                mockGeometry,
                mockProperties
        );

        when(spatialDataService.getPolygonAsGeoJson(spatialDataId)).thenReturn(serviceResponse);

        // Act
        ResponseEntity<GeoJsonResponseDTO> result = spatialDataController.getAsGeoJson(spatialDataId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(serviceResponse, result.getBody());
        assertEquals("Feature", result.getBody().type());
        assertNotNull(result.getBody().geometry());
        assertNotNull(result.getBody().properties());
        verify(spatialDataService, times(1)).getPolygonAsGeoJson(spatialDataId);
    }

    // ------------------------------------------------------------
    // Edge Cases - Service Exceptions
    // ------------------------------------------------------------
    @Test
    void getById_WhenServiceThrowsException_ShouldPropagateException() {
        // Arrange
        Long spatialDataId = 999L;
        when(spatialDataService.getById(spatialDataId))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Not found"));

        // Act & Assert
        assertThrows(jakarta.persistence.EntityNotFoundException.class,
                () -> spatialDataController.getById(spatialDataId));

        verify(spatialDataService, times(1)).getById(spatialDataId);
    }

    @Test
    void getAsGeoJson_WhenServiceThrowsException_ShouldPropagateException() {
        // Arrange
        Long spatialDataId = 999L;
        when(spatialDataService.getPolygonAsGeoJson(spatialDataId))
                .thenThrow(new IllegalArgumentException("No polygon found"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> spatialDataController.getAsGeoJson(spatialDataId));

        verify(spatialDataService, times(1)).getPolygonAsGeoJson(spatialDataId);
    }

    // ------------------------------------------------------------
    // Controller Initialization Test
    // ------------------------------------------------------------
    @Test
    void controller_WhenInitialized_ShouldNotBeNull() {
        // Arrange & Act & Assert
        assertNotNull(spatialDataController);
    }

    // ------------------------------------------------------------
    // Request Mapping Test (Basic verification)
    // ------------------------------------------------------------
    @Test
    void controller_ShouldHaveCorrectRequestMapping() {
        // This test just verifies the basic structure
        assertNotNull(SpatialDataController.class.getAnnotation(RequestMapping.class));
        assertEquals("/api/spatial-data",
                SpatialDataController.class.getAnnotation(RequestMapping.class).value()[0]);
    }

    // ------------------------------------------------------------
    // Additional Simple Tests
    // ------------------------------------------------------------
    @Test
    void createFromSerializer_WithNullRequest_ShouldCallService() {
        // Arrange
        SpatialDataSerializerRequestDTO request = null;
        SpatialDataResponseDTO serviceResponse = new SpatialDataResponseDTO(1L, null, null, null, null, null, null);

        when(spatialDataService.createFromSerializerFormat(request)).thenReturn(serviceResponse);

        // Act
        ResponseEntity<SpatialDataResponseDTO> result = spatialDataController.createFromSerializer(request);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(spatialDataService, times(1)).createFromSerializerFormat(request);
    }

    @Test
    void createFromDeserializer_WithNullRequest_ShouldCallService() {
        // Arrange
        SpatialDataDeserializerRequestDTO request = null;
        SpatialDataResponseDTO serviceResponse = new SpatialDataResponseDTO(1L, null, null, null, null, null, null);

        when(spatialDataService.createFromDeserializerFormat(request)).thenReturn(serviceResponse);

        // Act
        ResponseEntity<SpatialDataResponseDTO> result = spatialDataController.createFromDeserializer(request);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(spatialDataService, times(1)).createFromDeserializerFormat(request);
    }

    // ------------------------------------------------------------
    // Test for Service Dependency Injection
    // ------------------------------------------------------------
    @Test
    void controller_ShouldHaveServiceInjected() {
        // Arrange & Act & Assert
        assertNotNull(spatialDataController);
        // O Mockito injeta o mock automaticamente via @InjectMocks
    }
}