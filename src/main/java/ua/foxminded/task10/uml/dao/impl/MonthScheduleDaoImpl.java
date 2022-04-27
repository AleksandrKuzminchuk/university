package ua.foxminded.task10.uml.dao.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.MonthScheduleDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
import ua.foxminded.task10.uml.model.people.Student;
import ua.foxminded.task10.uml.model.people.Teacher;
import ua.foxminded.task10.uml.model.schedule.DaySchedule;
import ua.foxminded.task10.uml.model.schedule.MonthSchedule;
import ua.foxminded.task10.uml.model.schedule.WeekSchedule;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class MonthScheduleDaoImpl implements MonthScheduleDao {

    private final static Logger logger = Logger.getLogger(MonthScheduleDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final DayScheduleDaoImpl dayScheduleDao;
    private final WeekScheduleDaoImpl weekScheduleDao;

    public MonthScheduleDaoImpl(DataSource dataSource,
                                DayScheduleDaoImpl dayScheduleDao, WeekScheduleDaoImpl weekScheduleDao) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dayScheduleDao = dayScheduleDao;
        this.weekScheduleDao = weekScheduleDao;
    }

    @Override
    public Optional<MonthSchedule> save(MonthSchedule entity) {
        requiredNonNull(entity);
        logger.info(format("SAVING... %s", entity));
        final String SAVE_MONTH_SCHEDULE = "INSERT INTO month_schedules (month_name) VALUES (?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_MONTH_SCHEDULE, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getMonth());
            return statement;
        }, holder);
        Integer monthId = holder.getKey().intValue();
        entity.setId(monthId);
        Optional<MonthSchedule> result = Optional.of(entity);
        logger.info(format("SAVED %s SUCCESSFULLY", entity));
        return result;
    }

    @Override
    public Optional<MonthSchedule> findById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("FINDING MONTH SCHEDULE BY ID - %d", integer));
        final String FIND_WEEK_BY_MONTH_ID = "SELECT week_id FROM month_schedules_week_schedules WHERE month_id = ?";
        Optional<MonthSchedule> monthSchedule = Optional.of(findMonthById(integer)).
                orElseThrow(() -> new IllegalArgumentException(format("Can't find month schedule by id - %d", integer)));
        List<WeekSchedule> weekSchedulesId = jdbcTemplate.query(FIND_WEEK_BY_MONTH_ID, new Object[]{integer},
                new BeanPropertyRowMapper<>(WeekSchedule.class));
        List<WeekSchedule> weekSchedules = new ArrayList<>();
        weekSchedulesId.forEach(weekSchedule -> weekSchedules.add(weekScheduleDao.findById(weekSchedule.getId())
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find week schedule by id - %d", weekSchedule.getId())))));
        monthSchedule.get().setWeekSchedules(weekSchedules);
        logger.info(format("FOUND %s BY ID - %d", monthSchedule, integer));
        return monthSchedule;
    }

    @Override
    public boolean existsById(Integer integer) {
        throw new NotImplementedException("Method existsById not implemented");
    }

    @Override
    public List<MonthSchedule> findAll() {
        logger.info("FINDING... ALL MONTH SCHEDULES");
        final String FIND_ALL = "SELECT * FROM month_schedules";
        List<MonthSchedule> monthSchedules = jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(MonthSchedule.class));
        logger.info(format("FOUND ALL MONTH SCHEDULES - %d", monthSchedules.size()));
        return monthSchedules;
    }

    @Override
    public long count() {
        logger.info("FINDING... COUNT MONTH SCHEDULES");
        final String COUNT = "SELECT COUNT(*) FROM month_schedules";
        long count = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info(format("FOUND COUNT %d MONTH SCHEDULES", count));
        return count;
    }

    @Override
    public void deleteById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("DELETING... MONTH SCHEDULE BY ID - %d", integer));
        final String DELETE_BY_ID = "DELETE FROM month_schedules WHERE month_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(MonthSchedule.class));
        logger.info(format("DELETED MONTH SCHEDULE BY ID - %d SUCCESSFULLY", integer));
    }

    @Override
    public void delete(MonthSchedule entity) {
        requiredNonNull(entity);
        logger.info(format("DELETING... MONTH SCHEDULE BY MONTH NAME - %s", entity.getMonth()));
        final String DELETE_BY_MONTH_NAME = "DELETE FROM month_schedules WHERE month_name = ?";
        jdbcTemplate.update(DELETE_BY_MONTH_NAME, new Object[]{entity.getMonth()}, new BeanPropertyRowMapper<>(MonthSchedule.class));
        logger.info(format("DELETED MONTH SCHEDULE BY MONTH NAME - %s", entity.getMonth()));
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING... ALL MONTH SCHEDULES");
        final String DELETE_ALL = "DELETE FROM month_schedules";
        jdbcTemplate.update(DELETE_ALL, new BeanPropertyRowMapper<>(MonthSchedule.class));
        logger.info(format("DELETED ALL SCHEDULES SUCCESSFULLY"));
    }

    @Override
    public void saveAll(List<MonthSchedule> monthSchedules) {
        requiredNonNull(monthSchedules);
        logger.info(format("SAVING... ALL MONTH SCHEDULES - %d", monthSchedules.size()));
        monthSchedules.forEach(this::save);
        logger.info(format("SAVED ALL MONTH SCHEDULES - %d SUCCESSFULLY", monthSchedules.size()));
    }

    @Override
    public void update(String newMonthName, Integer monthId) {
        requiredNonNull(newMonthName);
        requiredNonNull(monthId);
        logger.info(format("UPDATING MONTH SCHEDULE BY MONTH ID - %d", monthId));
        final String UPDATE = "UPDATE month_schedules SET month_name = ? WHERE month_id = ?";
        jdbcTemplate.update(UPDATE, new Object[]{newMonthName, monthId}, new BeanPropertyRowMapper<>(MonthSchedule.class));
        logger.info(format("UPDATED MONTH SCHEDULE BY ID - %d SUCCESSFULLY", monthId));
    }

    @Override
    public void addWeekScheduleToMonth(MonthSchedule monthSchedule, WeekSchedule weekSchedules) {
        requiredNonNull(monthSchedule);
        requiredNonNull(weekSchedules);
        logger.info(format("ADDING... %d TO %d", weekSchedules.getId(), monthSchedule.getId()));
        final String ADD_WEEK_TO_MONTH = "INSERT INTO month_schedules_week_schedules (month_id, week_id) VALUES (?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(ADD_WEEK_TO_MONTH);
            statement.setInt(1, monthSchedule.getId());
            statement.setInt(2, weekSchedules.getId());
            statement.executeUpdate();
            logger.info(format("ADDED %d TO %d SUCCESSFULLY", weekSchedules.getId(), monthSchedule.getId()));
            return statement;
        });
    }

    @Override
    public void addWeekSchedulesToMonth(MonthSchedule monthSchedule, List<WeekSchedule> weekSchedules) {
        requiredNonNull(monthSchedule);
        requiredNonNull(weekSchedules);
        logger.info(format("ADDING... %d TO %d", weekSchedules.size(), monthSchedule.getId()));
        weekSchedules.forEach(weekSchedule -> addWeekScheduleToMonth(monthSchedule, weekSchedule));
        logger.info(format("ADDED %d TO %d SUCCESSFULLY", weekSchedules.size(), monthSchedule));
    }

    @Override
    public Optional<MonthSchedule> findByMonth(String month) {
        requiredNonNull(month);
        logger.info(format("FINDING... MONTH SCHEDULE BY MONTH NAME - %s", month));
        final String FIND_WEEK_BY_MONTH_NAME = "SELECT week_id FROM month_schedules_week_schedules mw JOIN month_schedules m" +
                "ON (mw.month_id = m.month_id) WHERE month_name = ?";
        Optional<MonthSchedule> monthSchedule = Optional.of(findMonthByName(month)).orElseThrow(
                () -> new IllegalArgumentException(format("Can't find month schedule by month name - %s", month)));
        List<WeekSchedule> weekSchedulesId = jdbcTemplate.query(FIND_WEEK_BY_MONTH_NAME, new Object[]{month},
                new BeanPropertyRowMapper<>(WeekSchedule.class));
        List<WeekSchedule> weekSchedules = new ArrayList<>();
        weekSchedulesId.forEach(weekSchedule -> weekSchedules.add(weekScheduleDao.findById(weekSchedule.getId())
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find week schedule by id %d", weekSchedule.getId())))));
        monthSchedule.get().setWeekSchedules(weekSchedules);
        logger.info(format("FOUND MONTH SCHEDULE %s BY MONTH NAME - %s", weekSchedules, month));
        return monthSchedule;
    }

    @Override
    public Optional<MonthSchedule> findTeacherMonthSchedule(Teacher teacher, String month) {
        requiredNonNull(teacher);
        requiredNonNull(month);
        logger.info(format("FINDING... TEACHER MONTH SCHEDULE FOR %s MONTH %s", teacher, month));
        final String FIND_TEACHER_DAYS = "SELECT ds.date FROM day_schedules_lessons sl JOIN day_schedules ds ON " +
                "(sl.day_schedule_id = ds.day_schedule_id) JOIN lessons l ON (ds.lesson_id = l.lesson_id) " +
                "JOIN teachers t ON (l.lesson_id = t.teacher_id) WHERE teacher_id =?";
        Optional<MonthSchedule> monthSchedule = Optional.of(findMonthByName(month)).orElseThrow(
                () -> new IllegalArgumentException(format("Can't find month schedule by month name - %s", month)));
        List<DaySchedule> dateTeacher = jdbcTemplate.query(FIND_TEACHER_DAYS, new Object[]{teacher.getId()},
                new BeanPropertyRowMapper<>(DaySchedule.class));
        List<DaySchedule> daySchedules = new ArrayList<>();
        dateTeacher.forEach(daySchedule -> daySchedules.add(dayScheduleDao.findTeacherDaySchedule(teacher, daySchedule.getDate())
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find teacher day schedule for %s %s", teacher, daySchedule.getDate())))));
        monthSchedule.get().setWeekSchedules(
                divideDaysByWeeks(daySchedules.stream().distinct().collect(Collectors.toList())));
        logger.info(format("FOUND TEACHER MONTH SCHEDULE %s", monthSchedule));
        return monthSchedule;
    }

    @Override
    public Optional<MonthSchedule> findStudentMonthSchedule(Student student, String month) {
        requiredNonNull(student);
        requiredNonNull(month);
        logger.info(format("FINDING... STUDENT MONTH SCHEDULE FOR %s MONTH %s", student, month));
        final String FIND_STUDENT_DAYS = "SELECT date FROM day_schedules_lessons dl JOIN day_schedules ds " +
                "ON (dl.day_schedule_id = ds.day_schedule_id) JOIN lessons l ON (dl.lesson_id = l.lesson_id) " +
                "JOIN groups g ON (l.group_id = g.group_id) JOIN students s ON (g.group_id = s.group_id) WHERE s.student_id = ?";
        Optional<MonthSchedule> monthSchedule = Optional.of(findMonthByName(month)).orElseThrow(
                () -> new IllegalArgumentException(format("Can't find month schedule by name month - %s", month)));
        List<DaySchedule> dayStudent = jdbcTemplate.query(FIND_STUDENT_DAYS, new Object[]{student.getId()},
                new BeanPropertyRowMapper<>(DaySchedule.class));
        List<DaySchedule> daySchedules = new ArrayList<>();
        dayStudent.forEach(daySchedule -> daySchedules.add(dayScheduleDao.findStudentDaySchedule(student, daySchedule.getDate())
                .orElseThrow(() -> new IllegalArgumentException("Can't find student day schedule"))));
        monthSchedule.get().setWeekSchedules(
                divideDaysByWeeks(daySchedules.stream().distinct().collect(Collectors.toList())));
        logger.info(format("FOUND STUDENT MONTH SCHEDULE %s", monthSchedule));
        return monthSchedule;
    }

    private Optional<MonthSchedule> findMonthById(Integer id) {
        requiredNonNull(id);
        logger.info(format("FINDING MONTH BY ID - %d", id));
        final String FIND_MONTH_BY_ID = "SELECT * FROM month_schedules WHERE month_id = ?";
        Optional<MonthSchedule> monthSchedule = Optional.ofNullable(jdbcTemplate.queryForObject(FIND_MONTH_BY_ID,
                new Object[]{id}, new BeanPropertyRowMapper<>(MonthSchedule.class)));
        logger.info(format("FOUND %s BY ID - %d SUCCESSFULLY", monthSchedule, id));
        return monthSchedule;
    }

    private Optional<MonthSchedule> findMonthByName(String monthName) {
        requiredNonNull(monthName);
        logger.info(format("FINDING MONTH BY MONTH NAME - %s", monthName));
        final String FIND_MONTH_BY_MONTH_NAME = "SELECT * FROM month_schedules WHERE month_name = ?";
        Optional<MonthSchedule> monthSchedule = Optional.ofNullable(jdbcTemplate.queryForObject(FIND_MONTH_BY_MONTH_NAME,
                new Object[]{monthName}, new BeanPropertyRowMapper<>(MonthSchedule.class)));
        logger.info(format("FOUND %s BY MONTH NAME - %s SUCCESSFULLY", monthSchedule, monthName));
        return monthSchedule;
    }

    private List<WeekSchedule> divideDaysByWeeks(List<DaySchedule> days) {
        Map<Integer, List<DaySchedule>> weeksOfMonth = days.stream().collect(Collectors.groupingBy(day ->
                day.getDate().get(WeekFields.of(Locale.getDefault()).weekOfMonth())));
        return weeksOfMonth.keySet().stream().map(k -> new WeekSchedule(k, k, weeksOfMonth.get(k)))
                .collect(Collectors.toList());
    }

    private void requiredNonNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException(ExceptionsHandlingConstants.ARGUMENT_IS_NULL);
        }
    }
}
