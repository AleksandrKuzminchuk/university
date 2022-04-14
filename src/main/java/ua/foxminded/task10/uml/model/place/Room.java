package ua.foxminded.task10.uml.model.place;

import ua.foxminded.task10.uml.model.schedule.Plannable;

import java.time.LocalDate;
import java.util.Objects;

public class Room implements Plannable {

    private Integer id;
    private String name;
    private Integer peopleCapacity;

    public Room() {
    }

    public Room(String name, Integer peopleCapacity) {
        this.name = name;
        this.peopleCapacity = peopleCapacity;
    }

    @Override
    public boolean isAvailable(LocalDate begin, LocalDate end) {
        return false;
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

    public Integer getPeopleCapacity() {
        return peopleCapacity;
    }

    public void setPeopleCapacity(Integer peopleCapacity) {
        this.peopleCapacity = peopleCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(id, room.id) && Objects.equals(name, room.name) && Objects.equals(peopleCapacity, room.peopleCapacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, peopleCapacity);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", peopleCapacity=" + peopleCapacity +
                '}';
    }
}
