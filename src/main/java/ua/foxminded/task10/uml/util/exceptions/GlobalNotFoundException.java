package ua.foxminded.task10.uml.util.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GlobalNotFoundException extends RuntimeException{
    public GlobalNotFoundException(String message) {
        super(message);
    }
}
