package ua.foxminded.task10.uml.model;


import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "students")
@EqualsAndHashCode(callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@AttributeOverride(name = "id", column = @Column(name = "student_id"))
public class Student extends Person {

    @ToString.Include
    @Column(name = "course")
    private Integer course;

    @ToString.Exclude
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private Group group;

}
