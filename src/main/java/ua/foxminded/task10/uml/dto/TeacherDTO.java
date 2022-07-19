package ua.foxminded.task10.uml.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {

    private Integer id;

    @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'Aleksandr'")
    @Pattern(regexp = "[A-Z]\\w{0,30}", message = "Name must start with a capital letter and be limited to 30 characters")
    private String firstName;

    @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'Jordan'")
    @Pattern(regexp = "[A-Z]\\w{0,30}", message = "Surname must start with a capital letter and be limited to 30 characters")
    private String lastName;
}
