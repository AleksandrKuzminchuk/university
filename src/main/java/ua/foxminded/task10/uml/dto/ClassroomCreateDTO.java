package ua.foxminded.task10.uml.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@ApiModel(value = "ClassroomCreateDTO")
public class ClassroomCreateDTO {

    @NotNull(message = "Can't be empty and consist on placeholders")
    @Min(value = 1, message = "Must be greater than 1. Range courses from 1 to 1000")
    @Max(value = 1000, message = "Must be less than 1000. Range courses from 1 to 1000")
    @ApiModelProperty(notes = "Unique number classroom", example = "125", required = true, position = 2)
    private Integer number;
}
