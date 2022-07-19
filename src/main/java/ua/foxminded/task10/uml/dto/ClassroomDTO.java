package ua.foxminded.task10.uml.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ClassroomDTO {

    private Integer id;

    @NotNull(message = "Can't be empty and consist on placeholders")
    @Min(value = 1, message = "Must be greater than 1. Range courses from 1 to 1000")
    @Max(value = 1000, message = "Must be less than 1000. Range courses from 1 to 1000")
    private Integer number;
}
