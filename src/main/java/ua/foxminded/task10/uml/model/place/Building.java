package ua.foxminded.task10.uml.model.place;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Building {

    private Integer id;
    private String name;
    private List<Room> rooms;

    public Building() {
        this.name = "";
        this.rooms = new ArrayList<>();
    }

    public Building(String name) {
        this.name = name;
        this.rooms = new ArrayList<>();
    }

    public Building(String name, List<Room> rooms) {
        this.name = name;
        this.rooms = rooms;
    }

    public Room addNewRoom(String roomName, Integer peopleCapacity){
        Room room = new Room(roomName, peopleCapacity);
        this.rooms.add(room);
        return room;
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

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Building building = (Building) o;
        return Objects.equals(id, building.id) && Objects.equals(name, building.name) && Objects.equals(rooms, building.rooms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, rooms);
    }

    @Override
    public String toString() {
        return "Building{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rooms=" + rooms +
                '}';
    }
}
