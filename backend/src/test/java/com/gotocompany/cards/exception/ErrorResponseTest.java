package com.gotocompany.cards.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorResponseTest {

    @SuppressWarnings("null")
    @Test
    void testErrorResponse() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Test error");
        errorResponse.setMessage("Test message");
        errorResponse.setPath("Test path");
        errorResponse.setStatus(200);
        errorResponse.setTimestamp(LocalDateTime.now());

        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getTimestamp());
        assertEquals(200, errorResponse.getStatus());
        assertEquals("Test error", errorResponse.getError());
        assertEquals("Test message", errorResponse.getMessage());
        assertEquals("Test path", errorResponse.getPath());
    }
}
