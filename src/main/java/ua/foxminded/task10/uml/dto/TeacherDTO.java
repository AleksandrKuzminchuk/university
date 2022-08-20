package ua.foxminded.task10.uml.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@ApiModel(value = "TeacherDTO")
public class TeacherDTO {
    @ApiModelProperty(notes = "Unique Id teacher", example = "65", position = 1)
    private Integer id;
    @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'Aleksandr'")
    @Pattern(regexp = "[A-Z][a-z]{3,30}", message = "Name must start with a capital letter and be limited to 30 characters")
    @ApiModelProperty(notes = "First name teacher", example = "Filip", position = 2)
    private String firstName;
    @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'Jordan'")
    @Pattern(regexp = "[A-Z][a-z]{3,30}", message = "Surname must start with a capital letter and be limited to 30 characters")
    @ApiModelProperty(notes = "Last name teacher", example = "Dorin", position = 3)
    private String lastName;

    public TeacherDTO(Integer id) {
        this.id = id;
    }

    public TeacherDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
