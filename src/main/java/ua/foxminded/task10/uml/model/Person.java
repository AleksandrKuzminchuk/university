package ua.foxminded.task10.uml.model;

import lombok.*;

import javax.persistence.*;

@Data
@MappedSuperclass
public abstract class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

}
