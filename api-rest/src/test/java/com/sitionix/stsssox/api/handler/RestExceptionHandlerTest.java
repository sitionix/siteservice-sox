package com.sitionix.stsssox.api.handler;

import com.app_afesox.stsssox.api_first.dto.ErrorDTO;
import com.sitionix.stsssox.domain.exception.AuthenticationRequiredException;
import com.sitionix.stsssox.domain.exception.SiteValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
