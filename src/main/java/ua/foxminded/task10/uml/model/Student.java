package ua.foxminded.task10.uml.model;

import java.util.Objects;

public class Student extends Person {

    private Integer course;
    private Group group;

    public Student() {
    }

    public Student(Integer id) {
        super(id);
    }

    public Student(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Student(String firstName, String lastName, Integer course) {
        super(firstName, lastName);
        this.course = course;
    }

    public Student(Integer id, String firstName, String lastName, Integer course, Group group) {
        super(id, firstName, lastName);
        this.course = course;
        this.group = group;
    }

    public Student(Integer id, String firstName, String lastName, Integer course) {
        super(id, firstName, lastName);
        this.course = course;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(course, student.course) && Objects.equals(group, student.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), course, group);
    }

    @Override
    public String toString() {
        return "Student{" + getId() + " " + getFirstName() + " " + getLastName() + " " + course + " " + group + "}";
    }
}
