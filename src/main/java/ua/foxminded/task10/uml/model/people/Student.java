package ua.foxminded.task10.uml.model.people;

import ua.foxminded.task10.uml.model.organization.Group;

public class Student extends Person{

    private Integer course;
    private Group group;

    public Student() {
    }

    public Student(String firstName, String lastName) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    public Student(String firstName, String lastName, Integer course) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.course = course;
    }

    public Student(Integer id, String firstName, String lastName) {
        this.setId(id);
        this.setFirstName(firstName);
        this.setLastName(lastName);
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
    public String toString() {
        return "Student{" + getFirstName() + " " + getLastName() + " " + course + "}";
    }
}
