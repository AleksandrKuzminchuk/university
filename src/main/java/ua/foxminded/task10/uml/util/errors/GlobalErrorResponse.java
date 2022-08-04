package ua.foxminded.task10.uml.util.errors;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "GlobalErrorResponse", description = "Return exception GlobalErrorResponse")
public class GlobalErrorResponse {

    @ApiModelProperty(value = "Exception class name", example = "GlobalNotValidException", position = 1)
    private String className;
    @ApiModelProperty(value = "Exception field name", example = "firstName", position = 2)
    private String fieldName;
    @ApiModelProperty(value = "Exception message", example = "Can't be empty and consist on placeholders", position = 3)
    private String message;
    @ApiModelProperty(value = "Date and time when was exception", example = "2022-07-29 19:32", position = 4)
    private String dateTime;
}
