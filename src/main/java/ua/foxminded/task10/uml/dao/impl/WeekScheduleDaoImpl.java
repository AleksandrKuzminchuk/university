package ua.foxminded.task10.uml.dao.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.DayScheduleDao;
import ua.foxminded.task10.uml.dao.WeekScheduleDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
import ua.foxminded.task10.uml.model.schedule.DaySchedule;
import ua.foxminded.task10.uml.model.schedule.WeekSchedule;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class WeekScheduleDaoImpl implements WeekScheduleDao {

    private final static Logger logger = Logger.getLogger(WeekScheduleDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final DayScheduleDao dayScheduleDao;

    public WeekScheduleDaoImpl(DataSource dataSource, DayScheduleDao dayScheduleDao) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dayScheduleDao = dayScheduleDao;
    }

    @Override
    public Optional<WeekSchedule> save(WeekSchedule entity) {
        requiredNonNull(entity);
        logger.info(format("SAVING... %s", entity));
        final String SAVE_WEEK_SCHEDULE = "INSERT INTO week_schedules (week_number) VALUES (?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_WEEK_SCHEDULE, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entity.getWeekNumber());
            return statement;
        }, holder);
        Integer weekScheduleId = holder.getKey().intValue();
        entity.setId(weekScheduleId);
        Optional<WeekSchedule> result = Optional.of(entity);
        logger.info(format("SAVED %s SUCCESSFULLY", entity));
        return result;
    }

    @Override
    public Optional<WeekSchedule> findById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("FINDING WEEK SCHEDULE BY ID - %d", integer));
        final String FIND_BY_ID = "SELECT day_schedule_id FROM week_schedules_day_schedules WHERE week_id = ?";
        Optional<WeekSchedule> weekSchedule = Optional.of(findWeekById(integer))
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find week by id - %d",integer)));
        List<DaySchedule> daySchedulesId = jdbcTemplate.query(FIND_BY_ID, new Object[]{integer},
                new BeanPropertyRowMapper<>(DaySchedule.class));
        List<DaySchedule> daySchedulesForWeek = new ArrayList<>();
        daySchedulesId.forEach(daySchedule -> daySchedulesForWeek.add(dayScheduleDao.findById(daySchedule.getId())
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find day schedule by id - %d",daySchedule.getId())))));
        weekSchedule.get().setDaySchedules(daySchedulesForWeek);
        logger.info(format("FOUND %s BY ID - %d", weekSchedule, integer));
        return weekSchedule;
     }

    @Override
    public boolean existsById(Integer integer) {

        throw new NotImplementedException("Method existsById not implemented");
    }

    @Override
    public List<WeekSchedule> findAll() {
        logger.info("FINDING... ALL WEEK SCHEDULES");
        final String FIND_ALL = "SELECT * FROM week_schedules";
        List<WeekSchedule> weekSchedules = jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(WeekSchedule.class));
        logger.info(format("FOUND ALL WEEK SCHEDULES - %d", weekSchedules.size()));
        return weekSchedules;
    }

    @Override
    public long count() {
        logger.info("FINDING... COUNT WEEK SCHEDULES");
        final String COUNT = "SELECT COUNT(*) FROM week_schedules";
        long count = jdbcTemplate.queryForObject(COUNT,Long.class);
        logger.info(format("FOUND %d WEEK SCHEDULES", count));
        return count;
    }

    @Override
    public void deleteById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("DELETING WEEK SCHEDULE BY ID - %d", integer));
        final String DELETE_BY_ID = "DELETE FROM week_schedules WHERE week_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(WeekSchedule.class));
        logger.info(format("DELETED WEEK SCHEDULE BY ID - %d SUCCESSFULLY", integer));
    }

    @Override
    public void delete(WeekSchedule entity) {
        throw new NotImplementedException("Method delete not implemented");
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING... ALL WEEK SCHEDULES");
        final String DELETE_ALL = "DELETE FROM week_schedules";
        jdbcTemplate.update(DELETE_ALL, new BeanPropertyRowMapper<>(WeekSchedule.class));
        logger.info("DELETED ALL WEEK SCHEDULES SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<WeekSchedule> weekSchedules) {
        requiredNonNull(weekSchedules);
        logger.info(format("SAVING ALL WEEK SCHEDULES - %d", weekSchedules.size()));
        weekSchedules.forEach(this::save);
        logger.info(format("SAVED ALL WEEK SCHEDULES  - %d SUCCESSFULLY", weekSchedules.size()));
    }

    @Override
    public void update(Integer newWeekNumber, Integer weekId) {
        requiredNonNull(newWeekNumber);
        requiredNonNull(weekId);
        logger.info(format("UPDATING WEEK SCHEDULE ON %d BY ID - %d", newWeekNumber, weekId));
        final String UPDATE = "UPDATE week_schedules SET week_number = ? WHERE week_id =?";
        jdbcTemplate.update(UPDATE, new Object[]{newWeekNumber, weekId}, new BeanPropertyRowMapper<>(WeekSchedule.class));
        logger.info(format("UPDATED WEEK SCHEDULE ON %d BY ID - %d SUCCESSFULLY", newWeekNumber, weekId));
    }

    @Override
    public void addDayScheduleToWeek(WeekSchedule weekSchedule, DaySchedule daySchedules) {
        requiredNonNull(weekSchedule);
        requiredNonNull(daySchedules);
        logger.info(format("ADDING... %s To %s", daySchedules, weekSchedule));
        final String ADD_DAY_SCHEDULE_TO_WEEK = "INSERT INTO week_schedules_day_schedules (week_id, day_schedule_id) VALUES(?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(ADD_DAY_SCHEDULE_TO_WEEK);
            statement.setInt(1, weekSchedule.getId());
            statement.setInt(2, daySchedules.getId());
            statement.executeUpdate();
            logger.info(format("ADDED %s TO %s SUCCESSFULLY", daySchedules, weekSchedule));
            return statement;
        });
    }

    @Override
    public void addDaySchedulesToWeek(WeekSchedule weekSchedule, List<DaySchedule> daySchedules) {
        requiredNonNull(weekSchedule);
        requiredNonNull(daySchedules);
        logger.info(format("ADDING... %d TO %s", daySchedules.size(), weekSchedule));
        daySchedules.forEach(daySchedule -> addDayScheduleToWeek(weekSchedule, daySchedule));
        logger.info(format("ADDED... %d TO %s SUCCESSFULLY", daySchedules.size(), weekSchedule));
    }

    private Optional<WeekSchedule> findWeekById(Integer id){
        requiredNonNull(id);
        logger.info(format("FINDING WEEK BY ID - %d", id));
        final String FIND_WEEK_BY_ID = "SELECT * FROM week_schedules WHERE week_id = ?";
        Optional<WeekSchedule> weekSchedule = Optional.ofNullable(jdbcTemplate.queryForObject(
                FIND_WEEK_BY_ID, new Object[]{id}, new BeanPropertyRowMapper<>(WeekSchedule.class)
        ));
        logger.info(format("FOUND %s BY ID - %d", weekSchedule, id));
        return weekSchedule;
    }

    private void requiredNonNull(Object o){
        if (o == null){
            throw new IllegalArgumentException(ExceptionsHandlingConstants.ARGUMENT_IS_NULL);
        }
    }
}
