package ua.foxminded.task10.uml.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "groups")
@JsonRootName(value = "group")
@ToString(onlyExplicitlyIncluded = true)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = Integer.class)
public class Group {

    @Id
    @ToString.Include
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @ToString.Include
    @Column(name = "group_name", unique = true)
    @Pattern(regexp = "[A-Z]-\\d{1,3}", message = "Must be 'B-1 or 'G-152'")
    private String name;

    @Singular
    @ToString.Exclude
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<Student> students;

    public Group(@NonNull Integer id) {
        this.id = id;
    }

    public Group(@NonNull String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Group group = (Group) o;
        return Objects.equals(getId(), group.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
