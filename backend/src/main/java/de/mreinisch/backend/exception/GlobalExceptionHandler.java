package de.mreinisch.backend.exception;

import jakarta.validation.ConstraintViolationException;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String UN_ERR= "Unerwarteter Fehler: ";

    @Override
    protected @NullMarked ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                              HttpHeaders headers,
                                                                              HttpStatusCode status,
                                                                              WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<FieldError> allErrors = ex.getBindingResult().getFieldErrors();

        allErrors.forEach(error -> {
            String fieldName = error.getField();
            String errorMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppUserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleAppUserNotFound(AppUserNotFound exception){
        return UN_ERR + exception.getMessage();
    }

    @ExceptionHandler(CdCollectionNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCdCollectionNotFound(CdCollectionNotFound exception){
        return UN_ERR + exception.getMessage();
    }

    @ExceptionHandler(CdNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCdNotFound(CdNotFound exception){
        return UN_ERR + exception.getMessage();
    }

    @ExceptionHandler(BarcodeNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleBarcodeNotFound(BarcodeNotFound exception){
        return "Suche erfolglos: " + exception.getMessage();
    }

    @ExceptionHandler(InquiryNotPossible.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handleInquiryNotPossible(InquiryNotPossible exception) {
        return exception.getMessage() + " Versuchen Sie es später nochmal!";
    }

    @ExceptionHandler(UnexpectedSeriousError.class)
    public String handleUnexpectedSeriousError(UnexpectedSeriousError exception) {
        return "Unerwarteter Ausnahmefehler: " + exception.getMessage();
    }
}
