package ua.foxminded.task10.uml.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "subjects")
public class Subject {

    @Id
    @Column(name = "subject_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Integer id;

    @Column(name = "subject_name")
    @NonNull
    @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'GEOMETRY'")
    private String name;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "teachers_subjects",
    joinColumns = @JoinColumn(name = "subject_id"),
    inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    @ToString.Exclude
    @NonNull
    private List<Teacher> teachers;

    public Subject(@NonNull Integer id) {
        this.id = id;
    }

    public Subject(@NonNull String name) {
        this.name = name;
    }

    public Subject(@NonNull Integer id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }
}
