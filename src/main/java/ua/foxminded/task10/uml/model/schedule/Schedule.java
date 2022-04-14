package ua.foxminded.task10.uml.model.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Schedule {

    private Integer id;
    private String name;
    private List<Slot> slots;
    private LocalDate startDate;
    private LocalDate endDate;

    public Schedule(){
        this.name = "";
        this.slots = new ArrayList<>();
    }

    public Schedule(String name, List<Slot> slots){
        this.name = name;
        this.slots = slots;
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

    public List<Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(id, schedule.id) && Objects.equals(name, schedule.name) && Objects.equals(slots, schedule.slots) && Objects.equals(startDate, schedule.startDate) && Objects.equals(endDate, schedule.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, slots, startDate, endDate);
    }
}
