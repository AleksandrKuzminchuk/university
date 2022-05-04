package ua.foxminded.task10.uml.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Event {

    private Integer id;
    private LocalDateTime localDateTime;
    private Subject subject;
    private Classroom classroom;
    private Group group;
    private Teacher teacher;

    public Event() {
    }

    public Event(Integer id) {
        this.id = id;
    }

    public Event(Subject subject, Classroom classroom, Group group, Teacher teacher, LocalDateTime localDateTime) {
        this.subject = subject;
        this.classroom = classroom;
        this.group = group;
        this.teacher = teacher;
        this.localDateTime = localDateTime;
    }

    public Event(Integer id, Subject subject, Classroom classroom, Group group, Teacher teacher, LocalDateTime localDateTime) {
        this.id = id;
        this.subject = subject;
        this.classroom = classroom;
        this.group = group;
        this.teacher = teacher;
        this.localDateTime = localDateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event lesson = (Event) o;
        return Objects.equals(id, lesson.id) && Objects.equals(subject, lesson.subject) && Objects.equals(classroom, lesson.classroom) && Objects.equals(group, lesson.group) && Objects.equals(teacher, lesson.teacher) && Objects.equals(localDateTime, lesson.localDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, classroom, group, teacher, localDateTime);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", subject=" + subject +
                ", classroom=" + classroom +
                ", groups=" + group +
                ", teacher=" + teacher +
                ", date=" + localDateTime +
                '}';
    }
}
