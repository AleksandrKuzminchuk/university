package ua.foxminded.task10.uml.model.organization;

import ua.foxminded.task10.uml.model.people.Student;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Group {

    private Integer id;
    private String name;
    private List<Student> students;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
        this.students = new ArrayList<>();
    }

    public Group(String name, List<Student> students) {
        this.name = name;
        this.students = new ArrayList<>();
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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(name, group.name) &&
                Objects.equals(students, group.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, students);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
