package ua.foxminded.task10.uml.model.schedule;

import ua.foxminded.task10.uml.model.curriculums.CourseSection;
import ua.foxminded.task10.uml.model.organization.Group;
import ua.foxminded.task10.uml.model.people.Teacher;
import ua.foxminded.task10.uml.model.place.Room;

import java.time.LocalDateTime;
import java.util.Objects;

public class Slot {

    private Integer id;
    private CourseSection courseSection;
    private Teacher teacher;
    private Group group;
    private Room room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String name;

    public Slot() {
    }

    public Slot(Teacher teacher, Group group, Room room, LocalDateTime startTime, LocalDateTime endTime) {
        this.teacher = teacher;
        this.group = group;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CourseSection getCourseSection() {
        return courseSection;
    }

    public void setCourseSection(CourseSection courseSection) {
        this.courseSection = courseSection;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return Objects.equals(id, slot.id) && Objects.equals(courseSection, slot.courseSection) && Objects.equals(teacher, slot.teacher) && Objects.equals(group, slot.group) && Objects.equals(room, slot.room) && Objects.equals(startTime, slot.startTime) && Objects.equals(endTime, slot.endTime) && Objects.equals(name, slot.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseSection, teacher, group, room, startTime, endTime, name);
    }
}
