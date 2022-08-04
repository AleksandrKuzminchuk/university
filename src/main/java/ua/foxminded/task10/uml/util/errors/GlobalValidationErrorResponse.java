package ua.foxminded.task10.uml.util.errors;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@RequiredArgsConstructor
public class GlobalValidationErrorResponse {

    private final List<GlobalErrorResponse> violations;
}
