package ua.foxminded.task10.uml.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {

    private Integer id;

    @NotBlank
    @Pattern(regexp = "[A-Z]-\\d{1,3}", message = "Must be 'B-1 or 'G-152'")
    private String name;
}
