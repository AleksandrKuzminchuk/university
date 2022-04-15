package ua.foxminded.task10.uml.model.place;

import java.util.Objects;

public class Classroom {

    private Integer id;
    private Integer number;
    private String describe;

    public Classroom() {
    }

    public Classroom(Integer number, String describe) {
        this.number = number;
        this.describe = describe;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Classroom classroom = (Classroom) o;
        return Objects.equals(id, classroom.id) && Objects.equals(number, classroom.number) && Objects.equals(describe, classroom.describe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, describe);
    }

    @Override
    public String toString() {
        return "Classroom{" +
                "id=" + id +
                ", number=" + number +
                ", describe='" + describe + '\'' +
                '}';
    }
}
