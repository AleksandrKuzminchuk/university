package ua.foxminded.task10.uml.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {

    private Integer id;

    @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'GEOMETRY'")
    @Pattern(regexp = "[A-Z]{1,30}", message = "All letters must be capital and be limited to 30 characters. Hint-'GEOMETRY'")
    private String name;
}
