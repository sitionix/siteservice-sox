package com.sitionix.stsssox.api.handler;

import com.app_afesox.stsssox.api_first.dto.ErrorDTO;
import com.sitionix.stsssox.domain.exception.AuthenticationRequiredException;
import com.sitionix.stsssox.domain.exception.SiteValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidation(final MethodArgumentNotValidException exception) {
        final FieldError firstFieldError = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .orElse(null);

        final String details;
        if (firstFieldError == null) {
            details = "Validation failed";
        } else if ("name".equals(firstFieldError.getField())
                && ("NotNull".equals(firstFieldError.getCode()) || "NotBlank".equals(firstFieldError.getCode()))) {
            details = "Site name is required";
        } else if ("name".equals(firstFieldError.getField()) && "Size".equals(firstFieldError.getCode())) {
            details = "Site name must be between 1 and 60 characters";
        } else {
            details = firstFieldError.getDefaultMessage();
        }

        return buildError(HttpStatus.BAD_REQUEST, details);
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
