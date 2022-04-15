package ua.foxminded.task10.uml.model.curriculums;

import ua.foxminded.task10.uml.model.organization.Group;
import ua.foxminded.task10.uml.model.people.Teacher;
import ua.foxminded.task10.uml.model.place.Classroom;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Event {

    private Integer id;
    private String name;
    private Classroom classroom;
    private List<Group> groups;
    private Teacher teacher;
    private Date date;

    public Event() {
    }

    public Event(String name, Classroom classroom, List<Group> groups, Teacher teacher, Date date) {
        this.name = name;
        this.classroom = classroom;
        this.groups = groups;
        this.teacher = teacher;
        this.date = date;
    }

    public Event(Integer id, String name, Classroom classroom, List<Group> groups, Teacher teacher, Date date) {
        this.id = id;
        this.name = name;
        this.classroom = classroom;
        this.groups = groups;
        this.teacher = teacher;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(name, event.name) && Objects.equals(classroom, event.classroom) && Objects.equals(groups, event.groups) && Objects.equals(teacher, event.teacher) && Objects.equals(date, event.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, classroom, groups, teacher, date);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", classroom=" + classroom +
                ", groups=" + groups +
                ", teacher=" + teacher +
                ", date=" + date +
                '}';
    }
}
