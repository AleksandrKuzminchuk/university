package ua.foxminded.task10.uml.model;


import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
@AttributeOverride(name = "id", column = @Column(name ="student_id"))
@AttributeOverride(name = "firstName", column = @Column(name = "first_name"))
@AttributeOverride(name = "lastName", column = @Column(name = "last_name"))
public class Student extends Person {

    @NonNull
    @EqualsAndHashCode.Include
    @Column(name = "course")
    @ToString.Include
    private Integer course;

    @ToString.Include
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private Group group;

    public Student(@NonNull Integer id) {
        super(id);
    }
}
