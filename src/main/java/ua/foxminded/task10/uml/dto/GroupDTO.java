package ua.foxminded.task10.uml.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class GroupDTO {

    private Integer id;

    @NotBlank
    @Pattern(regexp = "[A-Z]-\\d{1,3}", message = "Must be 'B-1 or 'G-152'")
    private String name;
}
