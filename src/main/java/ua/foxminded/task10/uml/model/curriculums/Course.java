package ua.foxminded.task10.uml.model.curriculums;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {

    private Integer id;
    private String name;
    private String description;
    private List<CourseSection> sections;

    public Course() {
        this.name = "";
        this.description = "";
        this.sections = new ArrayList<>();
    }

    public Course(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sections = new ArrayList<>();
    }

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
        this.sections = new ArrayList<>();
    }

    public CourseSection addNewSection(String name, String notes, String gradingBasis, Integer timeRequired){
        CourseSection section = new CourseSection(name, notes, gradingBasis, timeRequired);
        this.sections.add(section);
        return section;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CourseSection> getSections() {
        return sections;
    }

    public void setSections(List<CourseSection> sections) {
        this.sections = sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) && Objects.equals(name, course.name) && Objects.equals(description, course.description) && Objects.equals(sections, course.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, sections);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Course [id=").append(id).append(", name=").append(name).append(", description=")
                .append(description).append(", sections=").append(sections).append("]");
        return builder.toString();
    }
}
