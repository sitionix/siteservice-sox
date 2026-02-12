package com.sitionix.stsssox.api.handler;

import com.app_afesox.stsssox.api_first.dto.ErrorDTO;
import com.sitionix.stsssox.domain.exception.AuthenticationRequiredException;
import com.sitionix.stsssox.domain.exception.SiteValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(SiteValidationException.class)
    public ResponseEntity<ErrorDTO> handleSiteValidation(final SiteValidationException exception) {
        return buildError(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(AuthenticationRequiredException.class)
    public ResponseEntity<ErrorDTO> handleAuthenticationRequired(final RuntimeException exception) {
        return buildError(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handleNotReadable(final HttpMessageNotReadableException exception) {
        return buildError(HttpStatus.BAD_REQUEST, "Malformed request body");
    }

    private static ResponseEntity<ErrorDTO> buildError(final HttpStatus status, final String details) {
        return ResponseEntity.status(status)
                .body(ErrorDTO.builder()
                        .code(status.value())
                        .title(status.getReasonPhrase())
                        .details(details)
                        .build());
    }
}
