package ua.foxminded.task10.uml.util;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GlobalErrorResponse {

    String fieldName;
    String message;
    String dateTime;


}
