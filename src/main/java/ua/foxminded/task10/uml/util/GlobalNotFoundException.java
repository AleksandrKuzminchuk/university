package ua.foxminded.task10.uml.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GlobalNotFoundException extends RuntimeException{
    public GlobalNotFoundException(String message) {
        super(message);
    }
}
