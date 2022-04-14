package ua.foxminded.task10.uml.model.people;

public class Student extends Person{

    public Student() {
    }

    public Student(String firstName, String lastName) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    @Override
    public String toString() {
        return "Student{" + getFirstName() + " " + getLastName() + "}";
    }
}
