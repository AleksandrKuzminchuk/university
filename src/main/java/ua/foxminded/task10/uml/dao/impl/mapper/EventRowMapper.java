package ua.foxminded.task10.uml.dao.impl.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ua.foxminded.task10.uml.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class EventRowMapper implements ResultSetExtractor<List<Event>> {

    @Override
    public List<Event> extractData(ResultSet rs) throws SQLException, DataAccessException {
        return null;
    }
}