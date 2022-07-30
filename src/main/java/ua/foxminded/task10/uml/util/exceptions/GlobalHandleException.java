package ua.foxminded.task10.uml.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.util.errors.GlobalErrorResponse;
import ua.foxminded.task10.uml.util.errors.GlobalValidationErrorResponse;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ua.foxminded.task10.uml.util.formatters.DateTimeFormat.formatter;

@RestControllerAdvice
public class GlobalHandleException extends Exception{

    @ExceptionHandler(GlobalNotFoundException.class)
    public ResponseEntity<GlobalErrorResponse> handleExceptionNotFound(GlobalNotFoundException e){
        GlobalErrorResponse response = new GlobalErrorResponse();
        response.setFieldName(e.getClass().getSimpleName());
        response.setMessage(e.getMessage());
        response.setDateTime(LocalDateTime.now().format(formatter));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public GlobalValidationErrorResponse handleConstraintViolationException(ConstraintViolationException e){
        final List<GlobalErrorResponse> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new GlobalErrorResponse(
                                violation.getPropertyPath().toString(),
                                violation.getMessage(),
                                LocalDateTime.now().format(formatter)
                        )
                ).collect(Collectors.toList());
        return new GlobalValidationErrorResponse(violations);
    }

    @ExceptionHandler(GlobalNotNullException.class)
    public ResponseEntity<GlobalErrorResponse> handleExceptionNotNull(GlobalNotNullException e){
        GlobalErrorResponse response = new GlobalErrorResponse();
        response.setFieldName(e.getClass().getSimpleName());
        response.setDateTime(LocalDateTime.now().format(formatter));
        response.setMessage(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GlobalNotValidException.class)
    private ResponseEntity<GlobalErrorResponse> handleException(GlobalNotValidException e) {
        GlobalErrorResponse response = new GlobalErrorResponse(
                e.getClass().getSimpleName(),
                e.getMessage(),
                LocalDateTime.now().format(formatter)
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
