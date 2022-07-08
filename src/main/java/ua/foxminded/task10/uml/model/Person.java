package ua.foxminded.task10.uml.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Component
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
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

    public Person(Integer id) {
        this.id = id;
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
