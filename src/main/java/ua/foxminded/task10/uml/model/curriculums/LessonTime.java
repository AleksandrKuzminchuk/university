package ua.foxminded.task10.uml.model.curriculums;

import java.time.LocalTime;

public class LessonTime {

    private Integer id;
    private LocalTime startTime;
    private LocalTime endTime;

    public LessonTime() {
    }

    public LessonTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "LessonTime{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
