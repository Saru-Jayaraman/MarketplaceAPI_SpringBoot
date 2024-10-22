package se.lexicon.marketplaceapi_springboot.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RESTExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        StringBuilder details = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->{
            details.append(fieldError.getField());
            details.append(": ");
            details.append(fieldError.getDefaultMessage());
            details.append(", ");
        });
        ErrorDTO responseBody = new ErrorDTO(HttpStatus.BAD_REQUEST, details.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorDTO> handleConstraintViolationException(ConstraintViolationException ex) {
        StringBuilder details = new StringBuilder();
        ex.getConstraintViolations().forEach(constraintError ->{
            details.append(constraintError.getPropertyPath().toString());
            details.append(": ");
            details.append(constraintError.getMessage());
            details.append(", ");
        });
        ErrorDTO responseBody = new ErrorDTO(HttpStatus.BAD_REQUEST, details.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    @ExceptionHandler({IllegalArgumentException.class, WrongPasswordException.class})
    public ResponseEntity<ErrorDTO> handleCustomExceptions(Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDTO responseBody = new ErrorDTO(status, e.getMessage());
        return ResponseEntity.status(status).body(responseBody);
    }
}
