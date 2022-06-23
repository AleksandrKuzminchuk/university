package ua.foxminded.task10.uml.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teachers")
@AttributeOverride(name = "id", column = @Column(name = "teacher_id"))
@AttributeOverride(name = "firstName", column = @Column(name = "first_name"))
@AttributeOverride(name = "lastName", column = @Column(name = "last_name"))
public class Teacher extends Person {

    @Singular
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @ToString.Exclude
    @JoinTable(name = "teachers_subjects",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects;

    public Teacher(@NonNull Integer id) {
        super(id);
    }
}
