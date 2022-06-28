package ua.foxminded.task10.uml.model;

import lombok.*;

import javax.persistence.*;
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
    private String name;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "teachers_subjects",
    joinColumns = @JoinColumn(name = "subject_id"),
    inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    @ToString.Exclude
    @NonNull
    private List<Teacher> teachers;

    public Subject(@NonNull Integer id) {
        this.id = id;
    }
}
