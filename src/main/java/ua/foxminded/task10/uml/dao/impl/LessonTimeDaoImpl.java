package ua.foxminded.task10.uml.dao.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.LessonTimeDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
import ua.foxminded.task10.uml.model.curriculums.LessonTime;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class LessonTimeDaoImpl implements LessonTimeDao {

    private final static Logger logger = Logger.getLogger(LessonTimeDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public LessonTimeDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<LessonTime> save(LessonTime entity) {
        requiredNonNull(entity);
        logger.info(format("SAVING... LESSON TIME  - %s", entity));
        final String SAVE_LESSON_TIME = "INSERT INTO lesson_time (start_time, end_time) VALUES (?,?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_LESSON_TIME, Statement.RETURN_GENERATED_KEYS);
            statement.setTime(1, Time.valueOf(entity.getStartTime()));
            statement.setTime(2, Time.valueOf(entity.getEndTime()));
            return statement;
        }, holder);
        Integer lessonTimeId = holder.getKey().intValue();
        entity.setId(lessonTimeId);
        Optional<LessonTime> result = Optional.of(entity);
        logger.info(format("SAVED LESSON TIME - %s SUCCESSFULLY", entity));
        return result;
    }

    @Override
    public Optional<LessonTime> findById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("FINDING... LESSON TIME BY ID - %d", integer));
        final String FIND_BY_ID = "SELECT * FROM lesson_time WHERE lesson_time_id = ?";
        Optional<LessonTime> lessonTime = Optional.of(Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID, new Object[]{integer},
                new BeanPropertyRowMapper<>(LessonTime.class)))).orElseThrow(
                () -> new IllegalArgumentException(format("Can't find lesson time by - %d", integer)));
        logger.info(format("FOUND LESSON TIME - %s BY ID - %d", lessonTime, integer));
        return lessonTime;
    }

    @Override
    public boolean existsById(Integer integer) {

        throw new NotImplementedException("Method existsById not implemented");
    }

    @Override
    public List<LessonTime> findAll() {
        logger.info("FINDING... ALL LESSONS TIME");
        final String FIND_ALL_LESSON_TIME = "SELECT * FROM lesson_time";
        List<LessonTime> result = jdbcTemplate.query(FIND_ALL_LESSON_TIME, new BeanPropertyRowMapper<>(LessonTime.class));
        logger.info(format("FOUND ALL LESSONS TIME - %d", result.size()));
        return result;
    }

    @Override
    public long count() {
        logger.info("FINDING COUNT LESSONS TIME");
        final String COUNT = "SELECT COUNT(*) FROM lesson_time";
        long result = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info(format("FOUND COUNT - %d LESSONS TIME", result));
        return result;
    }

    @Override
    public void deleteById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("DELETING LESSON TIME BY ID - %d", integer));
        final String DELETE_BY_ID = "DELETE FROM lesson_time WHERE lesson_time_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(LessonTime.class));
        logger.info(format("DELETED LESSON TIME BY ID - %d SUCCESSFULLY", integer));
    }

    @Override
    public void delete(LessonTime entity) {
        throw new NotImplementedException("Method delete not implemented");
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING ALL LESSONS TIME");
        final String DELETE_ALL = "DELETE FROM lesson_time";
        jdbcTemplate.update(DELETE_ALL, new BeanPropertyRowMapper<>(LessonTime.class));
        logger.info("DELETED ALL LESSONS TIME SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<LessonTime> lessonTimes) {
        requiredNonNull(lessonTimes);
        logger.info(format("SAVING ALL LESSONS TIME - %d", lessonTimes.size()));
        lessonTimes.forEach(this::save);
        logger.info(format("SAVED ALL LESSONS TIME - %d SUCCESSFULLY", lessonTimes.size()));
    }

    @Override
    public void updateLessonTime(Integer lessonTimeId) {
        requiredNonNull(lessonTimeId);
        logger.info(format("UPDATING LESSON TIME BY ID - %d", lessonTimeId));
        final String UPDATE_LESSON_TIME = "UPDATE lesson_time SET start_time = ?, end_time = ? WHERE lesson_time_id = ?";
        jdbcTemplate.update(UPDATE_LESSON_TIME, new Object[]{lessonTimeId}, new BeanPropertyRowMapper<>(LessonTime.class));
        logger.info(format("UPDATED LESSON TIME BY ID - %d SUCCESSFULLY", lessonTimeId));
    }

    private void requiredNonNull(Object o){
        if (o == null){
            throw new IllegalArgumentException(ExceptionsHandlingConstants.ARGUMENT_IS_NULL);
        }
    }
}
