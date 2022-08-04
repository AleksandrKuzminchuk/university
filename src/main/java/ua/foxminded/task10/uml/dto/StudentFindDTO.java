package ua.foxminded.task10.uml.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "StudentFindDTO")
public class StudentFindDTO {

    @ApiModelProperty(notes = "First name student", example = "Jordan", position = 1)
    private String firstName;

    @ApiModelProperty(notes = "Last name student", example = "Gorden", position = 2)
    private String lastName;
}
