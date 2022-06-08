package ua.foxminded.task10.uml.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;


public class Event {

    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDateTime;

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
        this.dateTime = localDateTime;
    }

    public Event(Integer id, Subject subject, Classroom classroom, Group group, Teacher teacher, LocalDateTime localDateTime) {
        this.id = id;
        this.subject = subject;
        this.classroom = classroom;
        this.group = group;
        this.teacher = teacher;
        this.dateTime = localDateTime;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(dateTime, event.dateTime) && Objects.equals(startDateTime, event.startDateTime) && Objects.equals(endDateTime, event.endDateTime) && Objects.equals(subject, event.subject) && Objects.equals(classroom, event.classroom) && Objects.equals(group, event.group) && Objects.equals(teacher, event.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, startDateTime, endDateTime, subject, classroom, group, teacher);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", subject=" + subject +
                ", classroom=" + classroom +
                ", group=" + group +
                ", teacher=" + teacher +
                '}';
    }
}
