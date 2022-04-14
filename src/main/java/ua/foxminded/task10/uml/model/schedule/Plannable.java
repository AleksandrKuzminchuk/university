package ua.foxminded.task10.uml.model.schedule;

import java.time.LocalDate;

public interface Plannable {

    public boolean isAvailable(LocalDate begin, LocalDate end);
}
