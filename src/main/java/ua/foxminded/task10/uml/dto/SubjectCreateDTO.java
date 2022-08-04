package ua.foxminded.task10.uml.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@ApiModel(value = "SubjectCreateDTO")
public class SubjectCreateDTO {

    @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'GEOMETRY'")
    @Pattern(regexp = "[A-Z]{1,30}", message = "All letters must be capital and be limited to 30 characters. Hint-'GEOMETRY'")
    @ApiModelProperty(notes = "Unique subject name", example = "MATH")
    private String name;
}
