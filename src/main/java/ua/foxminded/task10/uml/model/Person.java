package ua.foxminded.task10.uml.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'Aleksandr'")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Can't be empty and consist on placeholders. Hint-'Jordan'")
    private String lastName;

    public Person(Integer id) {
        this.id = id;
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
