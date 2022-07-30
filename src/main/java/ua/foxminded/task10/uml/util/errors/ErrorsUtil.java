package ua.foxminded.task10.uml.util.errors;

import org.springframework.validation.FieldError;
import ua.foxminded.task10.uml.util.exceptions.GlobalNotValidException;

public class ErrorsUtil {
    public static void returnErrorsToClient(FieldError fieldError){

        throw new GlobalNotValidException(fieldError.getDefaultMessage());
    }
}
