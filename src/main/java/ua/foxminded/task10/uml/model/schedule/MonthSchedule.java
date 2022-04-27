package ua.foxminded.task10.uml.model.schedule;

import java.util.List;
import java.util.Objects;

public class MonthSchedule {

    private Integer id;
    private String month;
    private List<WeekSchedule> weekSchedules;

    public MonthSchedule() {
    }

    public MonthSchedule(String month) {
        this.month = month;
    }

    public MonthSchedule(Integer id, String month, List<WeekSchedule> weekSchedules) {
        this.id = id;
        this.month = month;
        this.weekSchedules = weekSchedules;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<WeekSchedule> getWeekSchedules() {
        return weekSchedules;
    }

    public void setWeekSchedules(List<WeekSchedule> weekSchedules) {
        this.weekSchedules = weekSchedules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthSchedule that = (MonthSchedule) o;
        return Objects.equals(id, that.id) && Objects.equals(month, that.month) && Objects.equals(weekSchedules, that.weekSchedules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, month, weekSchedules);
    }

    @Override
    public String toString() {
        return "MonthSchedule{" +
                "month='" + month + System.lineSeparator() +
                ", weekSchedules=" + weekSchedules +
                '}';
    }
}
