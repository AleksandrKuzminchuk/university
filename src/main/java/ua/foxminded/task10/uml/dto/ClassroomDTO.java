package ua.foxminded.task10.uml.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "ClassroomDTO")
public class ClassroomDTO {

    @ApiModelProperty(notes = "Unique Id classroom", example = "12", allowEmptyValue = true, position = 1)
    private Integer id;

    @NotNull(message = "Can't be empty and consist on placeholders")
    @Min(value = 1, message = "Must be greater than 1. Range courses from 1 to 1000")
    @Max(value = 1000, message = "Must be less than 1000. Range courses from 1 to 1000")
    @ApiModelProperty(notes = "Unique number classroom", example = "125", required = true, position = 2)
    private Integer number;

    public ClassroomDTO(Integer id) {
        this.id = id;
    }
}
