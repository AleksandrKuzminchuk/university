package ua.foxminded.task10.uml.model;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Component
public class Course {

    @Min(value = 1, message = "Must be range from 1 to 5")
    @Max(value = 5, message = "Must be range from 1 to 5")
    @NotNull(message = "Can't be empty and consist on placeholders")
    private Integer course;
}
