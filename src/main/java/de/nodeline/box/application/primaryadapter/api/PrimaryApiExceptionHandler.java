package de.nodeline.box.application.primaryadapter.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import de.nodeline.box.application.primaryadapter.api.exceptions.EntityNotProcessableException;
import de.nodeline.box.application.primaryadapter.api.exceptions.InvalidArgumentException;
import de.nodeline.box.application.primaryadapter.api.exceptions.ResourceNotFoundException;

@ControllerAdvice("de.nodeline.box.application.primaryadapter.api")
public class PrimaryApiExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<String> handleInvalidArgumentException(InvalidArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    
    @ExceptionHandler(EntityNotProcessableException.class)
    public ResponseEntity<String> handleEntityNotProcessableException(EntityNotProcessableException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }
}