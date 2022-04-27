package ua.foxminded.task10.uml.model.schedule;

import java.util.List;
import java.util.Objects;

public class WeekSchedule {

    private Integer id;
    private Integer weekNumber;
    private List<DaySchedule> daySchedules;

    public WeekSchedule() {
    }

    public WeekSchedule(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public WeekSchedule(Integer weekNumber, List<DaySchedule> daySchedules) {
        this.weekNumber = weekNumber;
        this.daySchedules = daySchedules;
    }

    public WeekSchedule(Integer id, Integer weekNumber, List<DaySchedule> daySchedules) {
        this.id = id;
        this.weekNumber = weekNumber;
        this.daySchedules = daySchedules;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public List<DaySchedule> getDaySchedules() {
        return daySchedules;
    }

    public void setDaySchedules(List<DaySchedule> daySchedules) {
        this.daySchedules = daySchedules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeekSchedule that = (WeekSchedule) o;
        return Objects.equals(id, that.id) && Objects.equals(weekNumber, that.weekNumber) && Objects.equals(daySchedules, that.daySchedules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, weekNumber, daySchedules);
    }

    @Override
    public String toString() {
        return "WeekSchedule{" +
                "weekNumber=" + weekNumber +
                ", daySchedules=" + daySchedules +
                '}';
    }
}
