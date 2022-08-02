package ua.foxminded.task10.uml.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.util.errors.GlobalErrorResponse;
import ua.foxminded.task10.uml.util.errors.ErrorResponse;
import ua.foxminded.task10.uml.util.errors.GlobalValidationErrorResponse;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ua.foxminded.task10.uml.util.formatters.DateTimeFormat.formatter;

@RestControllerAdvice
public class GlobalHandleException extends Exception{

    @ExceptionHandler(GlobalNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleExceptionNotFound(GlobalNotFoundException e){
        ErrorResponse response = ErrorResponse.builder().className(e.getClass().getSimpleName())
                .message(e.getMessage()).dateTime(LocalDateTime.now().format(formatter)).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public GlobalValidationErrorResponse handleConstraintViolationException(ConstraintViolationException e){
        final List<GlobalErrorResponse> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> GlobalErrorResponse.builder().className(violation.getClass().getName())
                                .fieldName(violation.getPropertyPath().toString()).message(violation.getMessage())
                                .dateTime(LocalDateTime.now().format(formatter)).build()
                ).collect(Collectors.toList());

        return new GlobalValidationErrorResponse(violations);
    }

    @ExceptionHandler(GlobalNotNullException.class)
    public ResponseEntity<ErrorResponse> handleExceptionNotNull(GlobalNotNullException e){
        ErrorResponse response = ErrorResponse.builder().className(e.getClass().getSimpleName())
                .message(e.getMessage()).dateTime(LocalDateTime.now().format(formatter)).build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GlobalNotValidException.class)
    private ResponseEntity<ErrorResponse> handleException(GlobalNotValidException e) {
        ErrorResponse response = ErrorResponse.builder().className(e.getClass().getSimpleName())
                .message(e.getMessage()).dateTime(LocalDateTime.now().format(formatter)).build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
