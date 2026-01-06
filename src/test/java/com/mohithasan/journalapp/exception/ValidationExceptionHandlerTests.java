package com.mohithasan.journalapp.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidationExceptionHandlerTest {

    @Test
    void shouldReturnFieldErrorsMap() {
        ValidationExceptionHandler handler = new ValidationExceptionHandler();

        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        FieldError fieldError = new FieldError(
                "object", "name", "name is required"
        );

        when(bindingResult.getFieldErrors()).thenReturn(
                java.util.List.of(fieldError)
        );

        ResponseEntity<Map<String, String>> response = handler.handle(ex);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("name is required", response.getBody().get("name"));
    }
}
