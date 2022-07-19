package ua.foxminded.task10.uml.util.formatters;

import java.time.format.DateTimeFormatter;

public final class DateTimeFormat {
    private DateTimeFormat() {}
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
}
