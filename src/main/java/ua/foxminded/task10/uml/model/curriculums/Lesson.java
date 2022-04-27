package ua.foxminded.task10.uml.model.curriculums;

import ua.foxminded.task10.uml.model.organization.Group;
import ua.foxminded.task10.uml.model.people.Teacher;
import ua.foxminded.task10.uml.model.place.Classroom;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Lesson {

    private Integer id;
    private Subject subject;
    private Classroom classroom;
    private Group group;
    private Teacher teacher;
    private LessonTime lessonTime;

    public Lesson() {
    }

    public Lesson(Integer id) {
        this.id = id;
    }

    public Lesson(Subject subject, Classroom classroom, Group group, Teacher teacher, LessonTime lessonTime) {
        this.subject = subject;
        this.classroom = classroom;
        this.group = group;
        this.teacher = teacher;
        this.lessonTime = lessonTime;
    }

    public Lesson(Integer id, Subject subject, Classroom classroom, Group group, Teacher teacher, LessonTime lessonTime) {
        this.id = id;
        this.subject = subject;
        this.classroom = classroom;
        this.group = group;
        this.teacher = teacher;
        this.lessonTime = lessonTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public LessonTime getLessonTime() {
        return lessonTime;
    }

    public void setLessonTime(LessonTime lessonTime) {
        this.lessonTime = lessonTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(id, lesson.id) && Objects.equals(subject, lesson.subject) && Objects.equals(classroom, lesson.classroom) && Objects.equals(group, lesson.group) && Objects.equals(teacher, lesson.teacher) && Objects.equals(lessonTime, lesson.lessonTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, classroom, group, teacher, lessonTime);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", subject=" + subject +
                ", classroom=" + classroom +
                ", groups=" + group +
                ", teacher=" + teacher +
                ", date=" + lessonTime +
                '}';
    }
}
