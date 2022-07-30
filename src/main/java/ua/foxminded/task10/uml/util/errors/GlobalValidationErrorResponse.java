package ua.foxminded.task10.uml.util.errors;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GlobalValidationErrorResponse {

    List<GlobalErrorResponse> violations;
}
