package ua.foxminded.task10.uml.model.people;

public class Student extends Person{

    private Integer course;

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

    @Override
    public String toString() {
        return "Student{" + getFirstName() + " " + getLastName() + " " + course + "}";
    }
}
