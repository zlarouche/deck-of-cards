package com.gotocompany.cards.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

public class GlobalExceptionHandlerTest {

    @SuppressWarnings("null")
    @Test
    void testHandleIllegalArgumentException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        IllegalArgumentException ex = new IllegalArgumentException("Test exception");
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());
        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgumentException(ex, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Test exception", response.getBody().getMessage());
    }

    @SuppressWarnings("null")
    @Test
    void testHandleIllegalStateException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        IllegalStateException ex = new IllegalStateException("Test exception");
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());
        ResponseEntity<ErrorResponse> response = handler.handleIllegalStateException(ex, request);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Test exception", response.getBody().getMessage());
    }

    @SuppressWarnings("null")
    @Test
    void testHandleGenericException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Exception ex = new Exception("Test exception");
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());
        ResponseEntity<ErrorResponse> response = handler.handleGenericException(ex, request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Test exception", response.getBody().getMessage());
    }

    @SuppressWarnings("null")
    @Test
    void testHandleValidationException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        
        // Create mock BindingResult with field errors
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("objectName", "name", "Name cannot be blank");
        FieldError fieldError2 = new FieldError("objectName", "age", "Age must be positive");
        List<FieldError> fieldErrors = Arrays.asList(fieldError1, fieldError2);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        
        // Create MethodArgumentNotValidException with mocked BindingResult
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        
        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());
        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex, request);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation Error", response.getBody().getError());
        assertEquals("name: Name cannot be blank, age: Age must be positive", response.getBody().getMessage());
        assertEquals(400, response.getBody().getStatus());
    }
}