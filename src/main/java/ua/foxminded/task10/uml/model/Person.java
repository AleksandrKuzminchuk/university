package ua.foxminded.task10.uml.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Getter
@Setter
@ToString
@MappedSuperclass
@RequiredArgsConstructor
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'Aleksandr'")
    @Pattern(regexp = "[A-Z]\\w{0,30}", message = "Name must start with a capital letter and be limited to 30 characters")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'Jordan'")
    @Pattern(regexp = "[A-Z]\\w{0,30}", message = "Surname must start with a capital letter and be limited to 30 characters")
    private String lastName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Person person = (Person) o;
        return id != null && Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
