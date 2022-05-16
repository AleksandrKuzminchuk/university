package ua.foxminded.task10.uml.model;

import java.util.Objects;

public class Student extends Person{

    private Integer course;
    private Integer groupId;

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

    public Student(Integer id, String firstName, String lastName, Integer course, Integer groupId) {
        super(id, firstName, lastName);
        this.course = course;
        this.groupId = groupId;
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

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(course, student.course) && Objects.equals(groupId, student.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), course, groupId);
    }

    @Override
    public String toString() {
        return "Student{" + getId() + " " + getFirstName() + " " + getLastName() + " " + course + " " + groupId + "}";
    }
}
