package ua.foxminded.task10.uml.model.organization;

import ua.foxminded.task10.uml.model.curriculums.Course;
import ua.foxminded.task10.uml.model.people.Student;
import ua.foxminded.task10.uml.model.schedule.Plannable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Group implements Plannable {

    private Integer id;
    private String name;
    private List<Student> students;
    private List<Course> requiredCourses;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
        this.students = new ArrayList<>();
        this.requiredCourses = new ArrayList<>();
    }

    public Group(String name, List<Student> students) {
        this.name = name;
        this.students = new ArrayList<>();
        this.requiredCourses = new ArrayList<>();
    }

    @Override
    public boolean isAvailable(LocalDate begin, LocalDate end) {
        return false;
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

    public List<Course> getRequiredCourses() {
        return requiredCourses;
    }

    public void setRequiredCourses(List<Course> requiredCourses) {
        this.requiredCourses = requiredCourses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(name, group.name) &&
                Objects.equals(students, group.students) && Objects.equals(requiredCourses, group.requiredCourses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, students, requiredCourses);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", students=" + students +
                ", requriedCourses=" + requiredCourses +
                '}';
    }
}
