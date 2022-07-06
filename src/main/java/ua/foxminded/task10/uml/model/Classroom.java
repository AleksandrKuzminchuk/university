package ua.foxminded.task10.uml.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "classrooms")
@ToString(onlyExplicitlyIncluded = true)
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classroom_id")
    @ToString.Include
    @NonNull
    private Integer id;

    @Column(name = "room_number", unique = true ,nullable = false)
    @NonNull
    @ToString.Include
    @Min(value = 1, message = "Must be greater than 1. Range courses from 1 to 1000")
    @Max(value = 1000, message = "Must be less than 1000. Range courses from 1 to 1000")
    @NotNull(message = "Can't be empty and consist on placeholders")
    private Integer number;

    public Classroom(@NonNull Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Classroom classroom = (Classroom) o;
        return Objects.equals(getId(), classroom.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
