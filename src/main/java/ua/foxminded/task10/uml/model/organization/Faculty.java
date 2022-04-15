package ua.foxminded.task10.uml.model.organization;

import ua.foxminded.task10.uml.model.curriculums.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Faculty {

    private Integer id;
    private String name;
    private List<Group> groups;
    private List<Subject> subjects;

    public Faculty() {
        this.name = "";
        this.groups = new ArrayList<>();
        this.subjects = new ArrayList<>();
    }

    public Faculty(String name) {
        this.name = name;
        this.groups = new ArrayList<>();
        this.subjects = new ArrayList<>();
    }

    public Faculty(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.groups = new ArrayList<>();
        this.subjects = new ArrayList<>();
    }

    public Group addNewGroup(String groupName){
        Group group = new Group(groupName);
        this.groups.add(group);
        return group;
    }

    public Subject addNewSubject(String subjectName){
        Subject subject = new Subject(subjectName);
        this.subjects.add(subject);
        return subject;
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

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return Objects.equals(id, faculty.id) && Objects.equals(name, faculty.name) && Objects.equals(groups, faculty.groups) && Objects.equals(subjects, faculty.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, groups, subjects);
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", groups=" + groups +
                ", subjects=" + subjects +
                '}';
    }
}
