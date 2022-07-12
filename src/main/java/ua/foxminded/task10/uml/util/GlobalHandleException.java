package ua.foxminded.task10.uml.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ua.foxminded.task10.uml.util.DateTimeFormat.formatter;

@ControllerAdvice
public class GlobalHandleException {

    @ResponseBody
    @ExceptionHandler(GlobalNotFoundException.class)
    public ResponseEntity<GlobalErrorResponse> handleExceptionNotFound(GlobalNotFoundException e){
        GlobalErrorResponse response = new GlobalErrorResponse();
        response.setFieldName(e.getClass().getName());
        response.setMessage(e.getMessage());
        response.setDateTime(LocalDateTime.now().format(formatter));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
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

    @ResponseBody
    @ExceptionHandler(GlobalNotNullException.class)
    public ResponseEntity<GlobalErrorResponse> handleExceptionNotNull(GlobalNotNullException e){
        GlobalErrorResponse response = new GlobalErrorResponse();
        response.setFieldName(e.getClass().getName());
        response.setDateTime(LocalDateTime.now().format(formatter));
        response.setMessage(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
