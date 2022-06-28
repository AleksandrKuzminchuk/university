package ua.foxminded.task10.uml.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "teachers")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AttributeOverride(name = "id", column = @Column(name = "teacher_id"))
public class Teacher extends Person {

    @Singular
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @ToString.Exclude
    @JoinTable(name = "teachers_subjects",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects;
}
