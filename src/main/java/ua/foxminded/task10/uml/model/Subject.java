package ua.foxminded.task10.uml.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @Column(name = "subject_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    @EqualsAndHashCode.Include
    @ToString.Include
    private Integer id;

    @Column(name = "subject_name")
    @NonNull
    @EqualsAndHashCode.Include
    @ToString.Include
    private String name;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "teachers_subjects",
    joinColumns = @JoinColumn(name = "subject_id"),
    inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    @NonNull
    @ToString.Exclude
    private List<Teacher> teachers;

    public Subject(@NonNull Integer id) {
        this.id = id;
    }
}
