package ua.foxminded.task10.uml.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@Table(name = "classrooms")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classroom_id")
    @NonNull
    private Integer id;

    @Column(name = "room_number", unique = true ,nullable = false)
    @NonNull
    @Min(value = 1, message = "Must be greater than 1. Range courses from 1 to 1000")
    @Max(value = 1000, message = "Must be less than 1000. Range courses from 1 to 1000")
    @NotNull(message = "Can't be empty and consist on placeholders")
    private Integer number;

    public Classroom(@NonNull Integer id) {
        this.id = id;
    }
}
