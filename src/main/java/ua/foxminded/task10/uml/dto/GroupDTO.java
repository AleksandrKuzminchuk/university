package ua.foxminded.task10.uml.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@ApiModel(value = "GroupDTO")
public class GroupDTO {

    @ApiModelProperty(notes = "Unique Id group", example = "45", allowEmptyValue = true, position = 1)
    private Integer id;

    @NotBlank
    @Pattern(regexp = "[A-Z]-\\d{1,3}", message = "Must be 'B-1 or 'G-152'")
    @ApiModelProperty(notes = "Unique name group", example = "G-10", required = true, position = 2)
    private String name;

    public GroupDTO(Integer id) {
        this.id = id;
    }

    public GroupDTO(String name) {
        this.name = name;
    }
}
