package ua.foxminded.task10.uml.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@ApiModel(value = "StudentUpdateDTO")
public class StudentUpdateDTO {

    @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'Aleksandr'")
    @Pattern(regexp = "[A-Z][a-z]{3,30}", message = "Name must start with a capital letter and be limited to 30 characters")
    @ApiModelProperty(notes = "First name student", example = "Jordan", position = 2)
    private String firstName;

    @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'Jordan'")
    @Pattern(regexp = "[A-Z][a-z]{3,30}", message = "Surname must start with a capital letter and be limited to 30 characters")
    @ApiModelProperty(notes = "Last name student", example = "Gorden", position = 3)
    private String lastName;

    @Min(value = 1, message = "Must be range from 1 to 5")
    @Max(value = 5, message = "Must be range from 1 to 5")
    @NotNull(message = "Can't be empty and consist on placeholders")
    @ApiModelProperty(notes = "Course student", example = "4", position = 4)
    private Integer course;

    @ApiModelProperty(notes = "Unique group student", example = "G-10", position = 5)
    private GroupDTO group;
}
