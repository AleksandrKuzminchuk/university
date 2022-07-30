package ua.foxminded.task10.uml.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "subjects")
@JsonRootName(value = "subject")
@ToString(onlyExplicitlyIncluded = true)
public class Subject {

    @Id
    @ToString.Include
    @Column(name = "subject_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ToString.Include
    @Column(name = "subject_name")
    @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'GEOMETRY'")
    @Pattern(regexp = "[A-Z]{1,30}", message = "All letters must be capital and be limited to 30 characters. Hint-'GEOMETRY'")
    private String name;

    @NonNull
    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "teachers_subjects",
    joinColumns = @JoinColumn(name = "subject_id"),
    inverseJoinColumns = @JoinColumn(name = "teacher_id"))
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Subject subject = (Subject) o;
        return Objects.equals(getId(), subject.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
