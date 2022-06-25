package ua.foxminded.task10.uml.model;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "classrooms")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classroom_id")
    @NonNull
    private Integer id;

    @Column(name = "room_number")
    @NonNull
    private Integer number;
}
