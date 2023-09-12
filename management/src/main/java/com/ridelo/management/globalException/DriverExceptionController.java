package com.ridelo.management.globalException;

import com.ridelo.management.model.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class DriverExceptionController {

    @ExceptionHandler(value = InvalidDriverIdException.class)
    public ResponseEntity<Object> exception(InvalidDriverIdException exception) {
        return new ResponseEntity<>(ExceptionResponse.builder().message(exception.getMessage())
                .dateTime(LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidRequestException.class)
    public ResponseEntity<Object> invalidRequest(InvalidRequestException exception) {
        return new ResponseEntity<>(ExceptionResponse.builder()
                .message("Invalid Request: "+exception.getMessage())
                .dateTime(LocalDateTime.now())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InactiveDriverException.class)
    public ResponseEntity<Object> inactiveDriverException(InactiveDriverException exception){
        return new ResponseEntity<>(ExceptionResponse.builder()
                .message(exception.getMessage())
                .dateTime(LocalDateTime.now())
                .build(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = DocumentVerificationException.class)
    public ResponseEntity<Object> documentVerificationException(DocumentVerificationException exception){
        return new ResponseEntity<>(ExceptionResponse.builder()
                .message(exception.getMessage())
                .dateTime(LocalDateTime.now())
                .build(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = InvalidDriverDataException.class)
    public ResponseEntity<Object> invalidDriverDataException(InvalidDriverDataException exception){
        return new ResponseEntity<>(ExceptionResponse.builder()
                .message(exception.getMessage())
                .dateTime(LocalDateTime.now())
                .build(),HttpStatus.CONFLICT);
    }
}
