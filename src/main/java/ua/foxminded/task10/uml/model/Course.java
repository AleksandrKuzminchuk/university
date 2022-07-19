package ua.foxminded.task10.uml.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Component
@JsonRootName(value = "course")
public class Course {

    @Min(value = 1, message = "Must be range from 1 to 5")
    @Max(value = 5, message = "Must be range from 1 to 5")
    @NotNull(message = "Can't be empty and consist on placeholders")
    private Integer course;
}
