package ua.foxminded.task10.uml.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    @NonNull
    private Integer id;

    @Column(name = "date_time")
    @ToString.Include
    @EqualsAndHashCode.Include
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;

    @Transient
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDateTime;

    @Transient
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDateTime;

    @EqualsAndHashCode.Include
    @ToString.Include
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "subject_id", referencedColumnName = "subject_id", unique = true)
    private Subject subject;

    @EqualsAndHashCode.Include
    @ToString.Include
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "classroom_id", referencedColumnName = "classroom_id", unique = true)
    private Classroom classroom;

    @EqualsAndHashCode.Include
    @ToString.Include
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "group_id", referencedColumnName = "group_id", unique = true)
    private Group group;

    @EqualsAndHashCode.Include
    @ToString.Include
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "teacher_id", referencedColumnName = "teacher_id", unique = true)
    private Teacher teacher;

    public Event(Integer event_id) {
        this.id = event_id;
    }
    public Event(Subject subjectByName, Classroom classroomByNumber, Group byGroupName,
                 Teacher teacherByNameSurname, LocalDateTime dateTime) {
        this.subject = subjectByName;
        this.classroom = classroomByNumber;
        this.group = byGroupName;
        this.teacher = teacherByNameSurname;
        this.dateTime = dateTime;
    }
}
