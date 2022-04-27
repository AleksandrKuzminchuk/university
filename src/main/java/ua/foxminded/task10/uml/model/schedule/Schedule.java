package ua.foxminded.task10.uml.model.schedule;

import java.util.List;

public class Schedule {

    private Integer id;
    private Integer year;
    private List<MonthSchedule> monthSchedules;

    public Schedule() {
    }

    public Schedule(Integer year) {
        this.year = year;
    }

    public Schedule(Integer year, List<MonthSchedule> monthSchedules) {
        this.year = year;
        this.monthSchedules = monthSchedules;
    }

    public Schedule(Integer id, Integer year, List<MonthSchedule> monthSchedules) {
        this.id = id;
        this.year = year;
        this.monthSchedules = monthSchedules;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<MonthSchedule> getMonthSchedules() {
        return monthSchedules;
    }

    public void setMonthSchedules(List<MonthSchedule> monthSchedules) {
        this.monthSchedules = monthSchedules;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "year=" + year + System.lineSeparator() +
                "monthSchedules=" + monthSchedules +
                '}';
    }
}
