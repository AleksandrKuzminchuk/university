package ua.foxminded.task10.uml.model.curriculums;

import ua.foxminded.task10.uml.model.organization.Group;
import ua.foxminded.task10.uml.model.people.Teacher;
import ua.foxminded.task10.uml.model.place.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Subject {

    private Integer id;
    private String name;
    private List<Course> courses;

    public Subject(){
        this.name = "";
        this.courses = new ArrayList<>();
    }

    public Subject(String name){
        this.name = name;
        this.courses = new ArrayList<>();
    }

    public Subject(Integer id, String name){
        this.id = id;
        this.name = name;
        this.courses = new ArrayList<>();
    }

    public Course addNewCourse(String name, String description){
        Course course = new Course(name, description);
        this.courses.add(course);
        return course;
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

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(id, subject.id) && Objects.equals(name, subject.name) && Objects.equals(courses, subject.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, courses);
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", courses=" + courses +
                '}';
    }
}
