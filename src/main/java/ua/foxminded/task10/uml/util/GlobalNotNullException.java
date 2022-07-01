package ua.foxminded.task10.uml.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GlobalNotNullException extends RuntimeException{

    public GlobalNotNullException(String message) {
        super(message);
    }
}
