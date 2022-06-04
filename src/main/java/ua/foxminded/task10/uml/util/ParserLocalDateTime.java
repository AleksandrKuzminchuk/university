package ua.foxminded.task10.uml.util;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

import static ua.foxminded.task10.uml.util.DateTimeFormat.formatter;

public class ParserLocalDateTime implements Converter<String, LocalDateTime> {
    @Override
    public LocalDateTime convert(String source) {
        return LocalDateTime.parse(source, formatter);
    }
}
