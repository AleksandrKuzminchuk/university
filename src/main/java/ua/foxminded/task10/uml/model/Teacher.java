package ua.foxminded.task10.uml.model;

import java.util.List;
import java.util.Objects;

public class Teacher extends Person{

    private List<Subject> teacherSubject;

    public Teacher() {
    }

    public Teacher(Integer id) {
        super(id);
    }

    public Teacher(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Teacher(Integer id, String firstName, String lastName) {
        super(id, firstName, lastName);
    }

    public List<Subject> getTeacherSubject() {
        return teacherSubject;
    }

    public void setTeacherSubject(List<Subject> teacherSubject) {
        this.teacherSubject = teacherSubject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(teacherSubject, teacher.teacherSubject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), teacherSubject);
    }

    @Override
    public String toString() {
        return "Teacher{" + getFirstName() + " " + getLastName() + "}";
    }
}
