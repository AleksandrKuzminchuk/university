package ua.foxminded.task10.uml.model.schedule;

import ua.foxminded.task10.uml.model.curriculums.Lesson;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class DaySchedule {

    private Integer id;
    private LocalDate date;
    private List<Lesson> lessons;

    public DaySchedule() {
    }

    public DaySchedule(LocalDate date) {
        this.date = date;
    }

    public DaySchedule(LocalDate date, List<Lesson> lessons) {
        this.date = date;
        this.lessons = lessons;
    }

    public DaySchedule(Integer id, LocalDate date, List<Lesson> lessons) {
        this.id = id;
        this.date = date;
        this.lessons = lessons;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DaySchedule that = (DaySchedule) o;
        return Objects.equals(id, that.id) && Objects.equals(date, that.date) && Objects.equals(lessons, that.lessons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, lessons);
    }

    @Override
    public String toString() {
        return "DaySchedule{" +
                "date=" + date +
                ", lessons=" + lessons +
                '}';
    }
}
