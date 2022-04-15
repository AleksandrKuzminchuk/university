package ua.foxminded.task10.uml.model.curriculums;

import java.util.Objects;

public class CourseSection {

    private Integer id;
    private String name;
    private String notes;
    private String gradingBasis;
    private Integer timeRequired;

    public CourseSection() {
        this.name = "";
        this.notes = "";
        this.gradingBasis = "";
    }

    public CourseSection(String name, String notes, String gradingBasis, int timeRequired) {
        this.name = name;
        this.notes = notes;
        this.gradingBasis = gradingBasis;
        this.timeRequired = timeRequired;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getGradingBasis() {
        return gradingBasis;
    }

    public void setGradingBasis(String gradingBasis) {
        this.gradingBasis = gradingBasis;
    }

    public Integer getTimeRequired() {
        return timeRequired;
    }

    public void setTimeRequired(Integer timeRequired) {
        this.timeRequired = timeRequired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseSection that = (CourseSection) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(notes, that.notes) && Objects.equals(gradingBasis, that.gradingBasis) && Objects.equals(timeRequired, that.timeRequired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, notes, gradingBasis, timeRequired);
    }

    @Override
    public String toString() {
        return "CourseSection{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                ", gradingBasis='" + gradingBasis + '\'' +
                ", timeRequired=" + timeRequired +
                '}';
    }
}
