package ua.foxminded.task10.uml.util.errors;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ApiModel(value = "GlobalValidationErrorResponse", description = "Return exception GlobalValidationErrorResponse")
public class GlobalValidationErrorResponse {

    @ApiModelProperty(value = "Exception violation", example = "{\n violations: [\n{\nclassName: ConstraintViolationImpl,\nfieldName: course,\nmessage: Can't be empty and consist on placeholders,\ndateTime: 2022-07-30 23:18\n}\n]")
    List<GlobalErrorResponse> violations;
}
