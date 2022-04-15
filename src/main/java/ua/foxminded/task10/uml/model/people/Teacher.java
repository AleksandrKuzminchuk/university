package ua.foxminded.task10.uml.model.people;

public class Teacher extends Person{

    public Teacher() {
    }

    public Teacher(String firstName, String lastName) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    public Teacher(Integer id, String firstName, String lastName) {
        this.setId(id);
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    @Override
    public String toString() {
        return "Teacher{" + getFirstName() + " " + getLastName() + "}";
    }
}
