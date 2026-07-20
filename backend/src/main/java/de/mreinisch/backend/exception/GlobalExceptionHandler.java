package de.mreinisch.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AppUserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleAppUserNotFound(AppUserNotFound exception){
        return "Unerwarteter Fehler: " + exception.getMessage();
    }

    @ExceptionHandler(CdCollectionNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleAppUserNotFound(CdCollectionNotFound exception){
        return "Unerwarteter Fehler: " + exception.getMessage();
    }
}
