package ua.foxminded.task10.uml.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class TeacherDTO {
    private Integer id;
    @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'Aleksandr'")
    @Pattern(regexp = "[A-Z][a-z]{3,30}", message = "Name must start with a capital letter and be limited to 30 characters")
    private String firstName;
    @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'Jordan'")
    @Pattern(regexp = "[A-Z][a-z]{3,30}", message = "Surname must start with a capital letter and be limited to 30 characters")
    private String lastName;
}
