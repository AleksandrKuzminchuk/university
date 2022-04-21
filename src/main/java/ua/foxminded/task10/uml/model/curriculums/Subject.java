package ua.foxminded.task10.uml.model.curriculums;

import ua.foxminded.task10.uml.model.organization.Group;
import ua.foxminded.task10.uml.model.people.Teacher;
import ua.foxminded.task10.uml.model.place.Classroom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Subject {

    private Integer id;
    private String name;
    private Teacher teacher;
    private List<Group> groups;
    private Classroom classroom;

    public Subject() {
    }

    public Subject(String name) {
        this.name = name;
    }

    public Subject(String name, Teacher teacher, List<Group> groups, Classroom classroom) {
        this.name = name;
        this.teacher = teacher;
        this.groups = groups;
        this.classroom = classroom;
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

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(id, subject.id) && Objects.equals(name, subject.name) && Objects.equals(teacher, subject.teacher) && Objects.equals(groups, subject.groups) && Objects.equals(classroom, subject.classroom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, teacher, groups, classroom);
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teacher=" + teacher +
                ", groups=" + groups +
                ", classroom=" + classroom +
                '}';
    }
}
