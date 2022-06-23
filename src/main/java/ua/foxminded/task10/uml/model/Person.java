package ua.foxminded.task10.uml.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    @EqualsAndHashCode.Include
    private Integer id;

    @NonNull
    @EqualsAndHashCode.Include
    private String firstName;

    @NonNull
    @EqualsAndHashCode.Include
    private String lastName;

    public Person(@NonNull Integer id) {
        this.id = id;
    }

    public Person(@NonNull String firstName, @NonNull String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
