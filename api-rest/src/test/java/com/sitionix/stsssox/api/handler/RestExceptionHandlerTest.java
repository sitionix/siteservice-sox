package com.sitionix.stsssox.api.handler;

import com.app_afesox.stsssox.api_first.dto.ErrorDTO;
import com.sitionix.stsssox.domain.exception.AuthenticationRequiredException;
import com.sitionix.stsssox.domain.exception.SiteValidationException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class RestExceptionHandlerTest {

    private RestExceptionHandler restExceptionHandler;

    @BeforeEach
    void setUp() {
        this.restExceptionHandler = new RestExceptionHandler();
    }

    @Test
    void givenSiteValidationException_whenHandleSiteValidation_thenReturnBadRequest() {
        //given
        final SiteValidationException exception = new SiteValidationException("Site name is required");
        final ResponseEntity<ErrorDTO> expected = this.getErrorResponse(HttpStatus.BAD_REQUEST, "Site name is required");

        //when
        final ResponseEntity<ErrorDTO> actual = this.restExceptionHandler.handleSiteValidation(exception);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void givenAuthenticationRequiredException_whenHandleAuthenticationRequired_thenReturnUnauthorized() {
        //given
        final AuthenticationRequiredException exception = new AuthenticationRequiredException("Authentication required");
        final ResponseEntity<ErrorDTO> expected = this.getErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication required");

        //when
        final ResponseEntity<ErrorDTO> actual = this.restExceptionHandler.handleAuthenticationRequired(exception);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void givenNameNotNullValidationError_whenHandleValidation_thenReturnNameRequired() {
        //given
        final MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        final BindingResult bindingResult = mock(BindingResult.class);
        final FieldError fieldError = mock(FieldError.class);
        final ResponseEntity<ErrorDTO> expected = this.getErrorResponse(HttpStatus.BAD_REQUEST, "Site name is required");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(fieldError.getField()).thenReturn("name");
        when(fieldError.getCode()).thenReturn("NotNull");

        //when
        final ResponseEntity<ErrorDTO> actual = this.restExceptionHandler.handleValidation(exception);

        //then
        assertThat(actual).isEqualTo(expected);
        verify(exception).getBindingResult();
        verify(bindingResult).getFieldErrors();
        verify(fieldError).getField();
        verify(fieldError).getCode();
        verifyNoMoreInteractions(exception, bindingResult, fieldError);
    }

    @Test
    void givenNameSizeValidationError_whenHandleValidation_thenReturnNameRangeMessage() {
        //given
        final MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        final BindingResult bindingResult = mock(BindingResult.class);
        final FieldError fieldError = mock(FieldError.class);
        final ResponseEntity<ErrorDTO> expected =
                this.getErrorResponse(HttpStatus.BAD_REQUEST, "Site name must be between 1 and 60 characters");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(fieldError.getField()).thenReturn("name");
        when(fieldError.getCode()).thenReturn("Size");

        //when
        final ResponseEntity<ErrorDTO> actual = this.restExceptionHandler.handleValidation(exception);

        //then
        assertThat(actual).isEqualTo(expected);
        verify(exception).getBindingResult();
        verify(bindingResult).getFieldErrors();
        verify(fieldError, times(2)).getField();
        verify(fieldError, times(3)).getCode();
        verifyNoMoreInteractions(exception, bindingResult, fieldError);
    }

    @Test
    void givenUnknownValidationError_whenHandleValidation_thenReturnDefaultFieldMessage() {
        //given
        final MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        final BindingResult bindingResult = mock(BindingResult.class);
        final FieldError fieldError = mock(FieldError.class);
        final ResponseEntity<ErrorDTO> expected =
                this.getErrorResponse(HttpStatus.BAD_REQUEST, "Any validation message");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(fieldError.getField()).thenReturn("description");
        when(fieldError.getCode()).thenReturn("Pattern");
        when(fieldError.getDefaultMessage()).thenReturn("Any validation message");

        //when
        final ResponseEntity<ErrorDTO> actual = this.restExceptionHandler.handleValidation(exception);

        //then
        assertThat(actual).isEqualTo(expected);
        verify(exception).getBindingResult();
        verify(bindingResult).getFieldErrors();
        verify(fieldError, times(2)).getField();
        verify(fieldError).getDefaultMessage();
        verifyNoMoreInteractions(exception, bindingResult, fieldError);
    }

    @Test
    void givenNoValidationErrors_whenHandleValidation_thenReturnValidationFailedMessage() {
        //given
        final MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        final BindingResult bindingResult = mock(BindingResult.class);
        final ResponseEntity<ErrorDTO> expected = this.getErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

        //when
        final ResponseEntity<ErrorDTO> actual = this.restExceptionHandler.handleValidation(exception);

        //then
        assertThat(actual).isEqualTo(expected);
        verify(exception).getBindingResult();
        verify(bindingResult).getFieldErrors();
        verifyNoMoreInteractions(exception, bindingResult);
    }

    @Test
    void givenNotReadableException_whenHandleNotReadable_thenReturnMalformedRequestBody() {
        //given
        final HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        final ResponseEntity<ErrorDTO> expected = this.getErrorResponse(HttpStatus.BAD_REQUEST, "Malformed request body");

        //when
        final ResponseEntity<ErrorDTO> actual = this.restExceptionHandler.handleNotReadable(exception);

        //then
        assertThat(actual).isEqualTo(expected);
        verifyNoMoreInteractions(exception);
    }

    private ResponseEntity<ErrorDTO> getErrorResponse(final HttpStatus status, final String details) {
        return ResponseEntity.status(status)
                .body(ErrorDTO.builder()
                        .code(status.value())
                        .title(status.getReasonPhrase())
                        .details(details)
                        .build());
    }
}
