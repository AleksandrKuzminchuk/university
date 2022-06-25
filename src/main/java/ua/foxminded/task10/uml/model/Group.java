package ua.foxminded.task10.uml.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "groups")
@ToString(onlyExplicitlyIncluded = true)
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    @ToString.Include
    @NonNull
    private Integer id;

    @ToString.Include
    @Column(name = "group_name")
    private String name;

    @Singular
    @ToString.Exclude
    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
            mappedBy = "group", fetch = FetchType.EAGER)
    private List<Student> students;

    public Group(@NonNull Integer id) {
        this.id = id;
    }
}
