package ua.foxminded.task10.uml.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "students")
@EqualsAndHashCode(callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@AttributeOverride(name = "id", column = @Column(name = "student_id"))
public class Student extends Person {

    @ToString.Include
    @Column(name = "course")
    @NonNull
    @Min(value = 1, message = "Must be range from 1 to 5")
    @Max(value = 5, message = "Must be range from 1 to 5")
    @NotNull(message = "Can't be empty and consist on placeholders")
    private Integer course;

    @ToString.Exclude
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private Group group;

    public Student(String firstName, String lastName, @NonNull Integer course) {
        super(firstName, lastName);
        this.course = course;
    }

    public Student(Integer id, Group group) {
        super(id);
        this.group = group;
    }

    public Student(@NonNull Integer course) {
        this.course = course;
    }

    public Student(Integer id, @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'Aleksandr'") String firstName, @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'Jordan'") String lastName, @NonNull Integer course, Group group) {
        super(id, firstName, lastName);
        this.course = course;
        this.group = group;
    }

}
