package ua.foxminded.task10.uml.model.organization;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class University {

    private Integer id;
    private String name;
    private List<Faculty> faculties;

    public University() {
        this.name = "";
        this.faculties = new ArrayList<>();
    }

    public University(String name) {
        this.name = name;
        this.faculties = new ArrayList<>();
    }

    public University(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.faculties = new ArrayList<>();
    }

    public Faculty addNewFaculty(String facultyName){
        Faculty faculty = new Faculty(facultyName);
        this.faculties.add(faculty);
        return faculty;
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

    public List<Faculty> getFaculties() {
        return faculties;
    }

    public void setFaculties(List<Faculty> faculties) {
        this.faculties = faculties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        University that = (University) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(faculties, that.faculties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, faculties);
    }

    @Override
    public String toString() {
        return "University{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", faculties=" + faculties +
                '}';
    }
}
