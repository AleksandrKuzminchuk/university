package ua.foxminded.task10.uml.model.people;

import ua.foxminded.task10.uml.model.curriculums.Subject;

public class Teacher extends Person{

    private Subject teacherSubject;

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

    public Subject getTeacherSubject() {
        return teacherSubject;
    }

    public void setTeacherSubject(Subject teacherSubject) {
        this.teacherSubject = teacherSubject;
    }

    @Override
    public String toString() {
        return "Teacher{" + getFirstName() + " " + getLastName() + "}";
    }
}
