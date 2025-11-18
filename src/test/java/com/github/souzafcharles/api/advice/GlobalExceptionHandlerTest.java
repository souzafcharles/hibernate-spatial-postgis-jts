package com.github.souzafcharles.api.advice;

import com.github.souzafcharles.api.utils.Messages;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // ------------------------------------------------------------
    // TEST: EntityNotFoundException
    // ------------------------------------------------------------
    @Test
    void handleEntityNotFound_WhenEntityNotFound_ShouldReturnNotFound() {
        // Arrange
        String errorMessage = "Entity not found";
        EntityNotFoundException ex = new EntityNotFoundException(errorMessage);

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                exceptionHandler.handleEntityNotFound(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().status());
        assertEquals(errorMessage, response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    // ------------------------------------------------------------
    // TEST: MethodArgumentNotValidException - CORRIGIDO
    // ------------------------------------------------------------
    @Test
    void handleValidationExceptions_WhenValidationFails_ShouldReturnBadRequest() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError = new FieldError("object", "field", "must not be null");
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));
        when(ex.getBindingResult()).thenReturn(bindingResult);

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                exceptionHandler.handleValidationExceptions(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
        assertEquals(Messages.VALIDATION_ERROR, response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    // ------------------------------------------------------------
    // TEST: ConstraintViolationException
    // ------------------------------------------------------------
    @Test
    void handleConstraintViolation_WhenConstraintViolated_ShouldReturnBadRequest() {
        // Arrange
        String errorMessage = "Constraint violation";
        ConstraintViolationException ex = new ConstraintViolationException(errorMessage, null);

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                exceptionHandler.handleConstraintViolation(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
        assertEquals(errorMessage, response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    // ------------------------------------------------------------
    // TEST: DataAccessException
    // ------------------------------------------------------------
    @Test
    void handleDataAccessException_WhenDataAccessFails_ShouldReturnInternalError() {
        // Arrange
        DataAccessException ex = mock(DataAccessException.class);

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                exceptionHandler.handleDataAccessException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().status());
        assertEquals(Messages.DATABASE_ERROR, response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    // ------------------------------------------------------------
    // TEST: DataIntegrityViolationException
    // ------------------------------------------------------------
    @Test
    void handleDataIntegrityViolation_WhenDataIntegrityFails_ShouldReturnConflict() {
        // Arrange
        DataIntegrityViolationException ex = mock(DataIntegrityViolationException.class);

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                exceptionHandler.handleDataIntegrityViolation(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().status());
        assertEquals(Messages.DATA_INTEGRITY_VIOLATION, response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    // ------------------------------------------------------------
    // TEST: HttpMessageNotReadableException
    // ------------------------------------------------------------
    @Test
    void handleHttpMessageNotReadable_WhenInvalidJson_ShouldReturnBadRequest() {
        // Arrange
        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                exceptionHandler.handleHttpMessageNotReadable(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
        assertEquals(Messages.INVALID_JSON_FORMAT, response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    // ------------------------------------------------------------
    // TEST: MethodArgumentTypeMismatchException - CORRIGIDO
    // ------------------------------------------------------------
    @Test
    void handleMethodArgumentTypeMismatch_WhenTypeMismatch_ShouldReturnBadRequest() {
        // Arrange
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getName()).thenReturn("id");
        when(ex.getRequiredType()).thenReturn((Class) Long.class);

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                exceptionHandler.handleMethodArgumentTypeMismatch(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
        assertTrue(response.getBody().message().contains("id"));
        assertTrue(response.getBody().message().contains("Long"));
        assertNotNull(response.getBody().timestamp());
    }

    // ------------------------------------------------------------
    // TEST: MissingServletRequestParameterException
    // ------------------------------------------------------------
    @Test
    void handleMissingParams_WhenParameterMissing_ShouldReturnBadRequest() {
        // Arrange
        MissingServletRequestParameterException ex =
                new MissingServletRequestParameterException("id", "Long");

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                exceptionHandler.handleMissingParams(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
        assertTrue(response.getBody().message().contains("id"));
        assertNotNull(response.getBody().timestamp());
    }

    // ------------------------------------------------------------
    // TEST: IllegalArgumentException
    // ------------------------------------------------------------
    @Test
    void handleIllegalArgument_WhenIllegalArgument_ShouldReturnBadRequest() {
        // Arrange
        String errorMessage = "Invalid argument";
        IllegalArgumentException ex = new IllegalArgumentException(errorMessage);

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                exceptionHandler.handleIllegalArgument(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
        assertEquals(errorMessage, response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    // ------------------------------------------------------------
    // TEST: RuntimeException
    // ------------------------------------------------------------
    @Test
    void handleRuntimeException_WhenRuntimeException_ShouldReturnInternalError() {
        // Arrange
        String errorMessage = "Runtime error";
        RuntimeException ex = new RuntimeException(errorMessage);

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                exceptionHandler.handleRuntimeException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().status());
        assertEquals(errorMessage, response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    // ------------------------------------------------------------
    // TEST: Generic Exception
    // ------------------------------------------------------------
    @Test
    void handleGenericException_WhenGenericException_ShouldReturnInternalError() {
        // Arrange
        Exception ex = new Exception("Generic error");

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                exceptionHandler.handleGenericException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().status());
        assertEquals(Messages.INTERNAL_ERROR_OCCURRED, response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    // ------------------------------------------------------------
    // TEST: ErrorResponse Record
    // ------------------------------------------------------------
    @Test
    void errorResponse_WhenCreated_ShouldHaveCorrectFields() {
        // Arrange
        int status = 404;
        String message = "Not found";
        String timestamp = LocalDateTime.now().format(formatter);

        // Act
        GlobalExceptionHandler.ErrorResponse errorResponse =
                new GlobalExceptionHandler.ErrorResponse(status, message, timestamp);

        // Assert
        assertNotNull(errorResponse);
        assertEquals(status, errorResponse.status());
        assertEquals(message, errorResponse.message());
        assertEquals(timestamp, errorResponse.timestamp());
    }

    // ------------------------------------------------------------
    // TEST: Timestamp Format
    // ------------------------------------------------------------
    @Test
    void errorResponse_ShouldHaveValidTimestampFormat() {
        // Arrange
        String errorMessage = "Test error";
        EntityNotFoundException ex = new EntityNotFoundException(errorMessage);

        // Act
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                exceptionHandler.handleEntityNotFound(ex);

        // Assert
        assertNotNull(response.getBody());
        String timestamp = response.getBody().timestamp();

        // Verify timestamp is in ISO format
        assertDoesNotThrow(() -> LocalDateTime.parse(timestamp, formatter));
    }

    // ------------------------------------------------------------
    // TEST: Handler Initialization
    // ------------------------------------------------------------
    @Test
    void globalExceptionHandler_WhenInitialized_ShouldNotBeNull() {
        // Arrange & Act & Assert
        assertNotNull(exceptionHandler);
    }
}