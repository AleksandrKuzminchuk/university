package ua.foxminded.task10.uml.dao.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.MonthScheduleDao;
import ua.foxminded.task10.uml.dao.ScheduleDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
import ua.foxminded.task10.uml.model.schedule.MonthSchedule;
import ua.foxminded.task10.uml.model.schedule.Schedule;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class ScheduleDaoImpl implements ScheduleDao {

    private final static Logger logger = Logger.getLogger(ScheduleDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final MonthScheduleDao monthScheduleDao;

    public ScheduleDaoImpl(DataSource dataSource, MonthScheduleDao monthScheduleDao) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.monthScheduleDao = monthScheduleDao;
    }

    @Override
    public Optional<Schedule> save(Schedule entity) {
        requiredNonNull(entity);
        logger.info(format("SAVING... %s", entity));
        final String SAVE_SCHEDULE = "INSERT INTO schedules (year) VALUES (?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_SCHEDULE, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entity.getId());
            return statement;
        }, holder);
        Integer scheduleId = holder.getKey().intValue();
        entity.setId(scheduleId);
        Optional<Schedule> result = Optional.of(entity);
        logger.info(format("SAVED %s SUCCESSFULLY", entity));
        return result;
    }

    @Override
    public Optional<Schedule> findById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("FINDING SCHEDULE BY ID - %d", integer));
        final String FIND_MONTH_FROM_SCHEDULE_BY_ID = "SELECT month_id FROM schedules_month_schedules WHERE schedule_id = ?";
        Optional<Schedule> schedule = Optional.of(findScheduleById(integer)).orElseThrow(
                () -> new IllegalArgumentException(format("Can't find schedule by id - %d", integer)));
        List<MonthSchedule> monthSchedulesId = jdbcTemplate.query(FIND_MONTH_FROM_SCHEDULE_BY_ID, new Object[]{integer},
                new BeanPropertyRowMapper<>(MonthSchedule.class));
        List<MonthSchedule> monthSchedules = new ArrayList<>();
        monthSchedulesId.forEach(monthSchedule -> monthSchedules.add(monthScheduleDao.findById(monthSchedule.getId())
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find month schedule by id - %d", monthSchedule.getId())))));
        schedule.get().setMonthSchedules(monthSchedules);
        logger.info(format("FOUND SCHEDULE %s BY ID - %d", schedule, integer));
        return schedule;
    }

    @Override
    public boolean existsById(Integer integer) {
        throw new NotImplementedException("Method existsById not implemented");
    }

    @Override
    public List<Schedule> findAll() {
        logger.info("FINDING... ALL SCHEDULES");
        final String FIND_ALL = "SELECT * FROM schedules";
        List<Schedule> schedules = jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Schedule.class));
        logger.info(format("FOUND ALL SCHEDULES %d", schedules.size()));
        return schedules;
    }

    @Override
    public long count() {
        logger.info(format("FINDING... COUNT SCHEDULES"));
        final String COUNT = "SELECT COUNT(*) FROM schedules";
        long count = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info(format("FOUND COUNT %d SCHEDULES SUCCESSFULLY", count));
        return count;
    }

    @Override
    public void deleteById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("DELETING... SCHEDULE BY ID - %d", integer));
        final String DELETE_BY_ID = "DELETE FROM schedules WHERE schedule_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(Schedule.class));
        logger.info(format("DELETED SCHEDULE BY ID - %d SUCCESSFULLY", integer));
    }

    @Override
    public void delete(Schedule entity) {
        requiredNonNull(entity);
        logger.info(format("DELETING... SCHEDULE BY YEAR - %s", entity));
        final String DELETE_BY_YEAR = "DELETE FROM schedules WHERE year = ?";
        jdbcTemplate.update(DELETE_BY_YEAR, new Object[]{entity}, new BeanPropertyRowMapper<>(Schedule.class));
        logger.info(format("DELETED SCHEDULE BY YEAR - %s SUCCESSFULLY", entity));
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING... ALL SCHEDULES");
        final String DELETE_ALL = "DELETE FROM schedules";
        jdbcTemplate.update(DELETE_ALL, new BeanPropertyRowMapper<>(Schedule.class));
        logger.info("DELETED ALL SCHEDULES SUCCESSFULLY");
    }

    @Override
    public void saveAll(List<Schedule> schedules) {
        requiredNonNull(schedules);
        logger.info(format("SAVING... ALL SCHEDULES - %d", schedules.size()));
        schedules.forEach(this::save);
        logger.info(format("SAVED ALL SCHEDULES %d SUCCESSFULLY", schedules.size()));
    }

    @Override
    public void update(Integer newYear, Integer scheduleId) {
        requiredNonNull(newYear);
        requiredNonNull(scheduleId);
        logger.info(format("UPDATING... SCHEDULE ON NEW YEAR - %d BY ID - %d", newYear, scheduleId));
        final String UPDATE = "UPDATE schedules SET year = ? WHERE schedule_id = ?";
        jdbcTemplate.update(UPDATE, new Object[]{newYear, scheduleId}, new BeanPropertyRowMapper<>(Schedule.class));
        logger.info(format("UPDATED SCHEDULE ON NEW YEAR - %d BY ID - %d SUCCESSFULLY", newYear, scheduleId));
    }

    @Override
    public void addMonthToSchedule(Schedule schedule, MonthSchedule monthSchedules) {
        requiredNonNull(schedule);
        requiredNonNull(monthSchedules);
        logger.info(format("ADDING... %s TO %s", monthSchedules, schedule));
        final String ADD_MONTH_TO_SCHEDULE = "INSERT INTO schedules_month_schedules (schedule_id, month_id) VALUES (?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(ADD_MONTH_TO_SCHEDULE);
            statement.setInt(1, schedule.getId());
            statement.setInt(2, monthSchedules.getId());
            statement.executeUpdate();
            logger.info(format("ADDED %s TO %s SUCCESSFULLY", schedule, monthSchedules));
            return statement;
        });
    }

    @Override
    public void addMonthsToSchedule(Schedule schedule, List<MonthSchedule> monthSchedules) {
        requiredNonNull(schedule);
        requiredNonNull(monthSchedules);
        logger.info(format("ADDING... %d TO %s", monthSchedules.size(), schedule));
        monthSchedules.forEach(monthSchedule -> addMonthToSchedule(schedule, monthSchedule));
        logger.info(format("ADDED %d TO %s SUCCESSFULLY", monthSchedules.size(), schedule));
    }

    @Override
    public Optional<Schedule> findByYear(Integer year) {
        requiredNonNull(year);
        logger.info(format("FINDING SCHEDULE BY YEAR - %d", year));
        final String FIND_MONTHS_FROM_SCHEDULE_BY_YEAR = "SELECT month_id FROM schedules_month_schedules sm" +
                "JOIN schedules s ON (sm.schedule_id = s.schedule_id) WHERE year = ?";
        Optional<Schedule> schedule = Optional.of(findScheduleByYear(year)).orElseThrow(
                () -> new IllegalArgumentException(format("Can't find schedule by year - %d", year)));
        List<MonthSchedule> monthSchedulesId = jdbcTemplate.query(FIND_MONTHS_FROM_SCHEDULE_BY_YEAR, new Object[]{year},
                new BeanPropertyRowMapper<>(MonthSchedule.class));
        List<MonthSchedule> monthSchedules = new ArrayList<>();
        monthSchedulesId.forEach(monthSchedule -> monthScheduleDao.findById(monthSchedule.getId()));
        schedule.get().setMonthSchedules(monthSchedules);
        logger.info(format("FOUND SCHEDULE %s BY YEAR - %d", schedule, year));
        return schedule;
    }

    private Optional<Schedule> findScheduleById(Integer id){
        requiredNonNull(id);
        logger.info(format("FINDING SCHEDULE BY ID - %d", id));
        final String FIND_SCHEDULE_BY_ID = "SELECT * FROM schedules WHERE schedule_id = ?";
        Optional<Schedule> schedule = Optional.ofNullable(jdbcTemplate.queryForObject(FIND_SCHEDULE_BY_ID,
                new Object[]{id}, new BeanPropertyRowMapper<>(Schedule.class)));
        logger.info(format("FOUND SCHEDULE %s BY ID - %d SUCCESSFULLY",schedule, id));
        return schedule;
    }

    private Optional<Schedule> findScheduleByYear(Integer year){
        requiredNonNull(year);
        logger.info(format("FINDING... SCHEDULE BY YEAR - %d", year));
        final String FIND_SCHEDULE_BY_YEAR = "SELECT * FROM schedules WHERE year = ?";
        Optional<Schedule> schedule = Optional.ofNullable(jdbcTemplate.queryForObject(FIND_SCHEDULE_BY_YEAR,
                new Object[]{year}, new BeanPropertyRowMapper<>(Schedule.class)));
        logger.info(format("FOUND SCHEDULE %s BY YEAR - %d", schedule, year));
        return schedule;
    }

    private void requiredNonNull(Object o){
        throw new IllegalArgumentException(ExceptionsHandlingConstants.ARGUMENT_IS_NULL);
    }
}
