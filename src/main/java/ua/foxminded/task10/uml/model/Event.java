package ua.foxminded.task10.uml.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events")
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    @NonNull
    private Integer id;

    @Column(name = "date_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;

    @Transient
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDateTime;

    @Transient
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDateTime;

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "subject_id", referencedColumnName = "subject_id")
    private Subject subject;

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "classroom_id", referencedColumnName = "classroom_id")
    private Classroom classroom;

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private Group group;

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "teacher_id", referencedColumnName = "teacher_id")
    private Teacher teacher;

    public Event(Subject subject, Classroom classroom, Group group, Teacher teacher, LocalDateTime dateTime) {
        this.subject = subject;
        this.classroom = classroom;
        this.group = group;
        this.teacher = teacher;
        this.dateTime = dateTime;
    }
}
