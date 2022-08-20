package ua.foxminded.task10.uml.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "events")
@JsonRootName(value = "event")
@ToString(onlyExplicitlyIncluded = true)
public class Event {

    @Id
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Integer id;

    @ToString.Include
    @Column(name = "date_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateTime;

    @OneToOne
    @ToString.Include
    @JoinColumn(name = "subject_id", referencedColumnName = "subject_id")
    private Subject subject;

    @OneToOne
    @ToString.Include
    @JoinColumn(name = "classroom_id", referencedColumnName = "classroom_id")
    private Classroom classroom;

    @OneToOne
    @ToString.Include
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private Group group;

    @OneToOne
    @ToString.Include
    @JoinColumn(name = "teacher_id", referencedColumnName = "teacher_id")
    private Teacher teacher;

    public Event(Subject subject, Classroom classroom, Group group, Teacher teacher, LocalDateTime dateTime) {
        this.subject = subject;
        this.classroom = classroom;
        this.group = group;
        this.teacher = teacher;
        this.dateTime = dateTime;
    }

    public Event(Subject subject, Classroom classroom, Group group, Teacher teacher) {
        this.subject = subject;
        this.classroom = classroom;
        this.group = group;
        this.teacher = teacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Event event = (Event) o;
        return Objects.equals(getId(), event.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
