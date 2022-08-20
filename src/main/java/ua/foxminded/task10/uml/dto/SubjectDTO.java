package ua.foxminded.task10.uml.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "SubjectDTO")
public class SubjectDTO {

    @ApiModelProperty(notes = "Unique Id subject", example = "52", allowEmptyValue = true, position = 1)
    private Integer id;

    @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'GEOMETRY'")
    @Pattern(regexp = "[A-Z]{1,30}", message = "All letters must be capital and be limited to 30 characters. Hint-'GEOMETRY'")
    @ApiModelProperty(notes = "Unique subject name", example = "MATH", position = 2)
    private String name;

    public SubjectDTO(Integer id) {
        this.id = id;
    }

    public SubjectDTO(String name) {
        this.name = name;
    }
}
