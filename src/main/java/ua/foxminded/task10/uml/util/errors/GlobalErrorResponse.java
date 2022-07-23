package ua.foxminded.task10.uml.util.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class GlobalErrorResponse {

    private String fieldName;
    private String message;
    private String dateTime;
}
