package ua.foxminded.task10.uml.dao.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.DayScheduleDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
import ua.foxminded.task10.uml.model.curriculums.Lesson;
import ua.foxminded.task10.uml.model.people.Person;
import ua.foxminded.task10.uml.model.people.Student;
import ua.foxminded.task10.uml.model.people.Teacher;
import ua.foxminded.task10.uml.model.schedule.DaySchedule;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class DayScheduleDaoImpl implements DayScheduleDao {

    private final static Logger logger = Logger.getLogger(DayScheduleDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final LessonDaoImpl lessonDao;

    public DayScheduleDaoImpl(DataSource dataSource, LessonDaoImpl lessonDao) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.lessonDao = lessonDao;
    }

    @Override
    public Optional<DaySchedule> save(DaySchedule entity) {
        requiredNonNull(entity);
        logger.info(format("SAVING... %s", entity));
        final String SAVE_DAY_SCHEDULE = "INSERT INTO day_schedules (date) VALUES (?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_DAY_SCHEDULE, Statement.RETURN_GENERATED_KEYS);
            statement.setDate(1, Date.valueOf(entity.getDate()));
            return statement;
        }, holder);
        Integer dayScheduleId = holder.getKey().intValue();
        entity.setId(dayScheduleId);
        Optional<DaySchedule> result = Optional.of(entity);
        logger.info(format("SAVED %s SUCCESSFULLY", entity));
        return result;
    }

    @Override
    public Optional<DaySchedule> findById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("FINDING... DAY SCHEDULE BY ID - %d", integer));
        final String FIND_BY_ID = "SELECT lesson_id FROM day_schedules_lessons WHERE day_schedule_id = ?";
        Optional<DaySchedule> daySchedule = Optional.of(findDayScheduleById(integer))
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find day schedule by id - %d", integer)));
        List<Lesson> lessonId = jdbcTemplate.query(FIND_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(Lesson.class));
        List<Lesson> dayLesson = new ArrayList<>();
        lessonId.forEach(lesson -> dayLesson.add(lessonDao.findById(lesson.getId())
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find day schedule by id - %d", lesson.getId())))));
        daySchedule.get().setLessons(dayLesson);
        logger.info(format("FOUND %s BY ID - %d", dayLesson, integer));
        return daySchedule;
    }

    @Override
    public boolean existsById(Integer integer) {

        throw new NotImplementedException("Method existsById not implemented");
    }

    @Override
    public List<DaySchedule> findAll() {
        logger.info("FINDING... ALL DAY SCHEDULES");
        final String FIND_ALL = "SELECT * FROM day_schedules";
        List<DaySchedule> daySchedules = jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(DaySchedule.class));
        logger.info(format("FOUND ALL DAY SCHEDULES %d SUCCESSFULLY", daySchedules.size()));
        return daySchedules;
    }

    @Override
    public long count() {
        logger.info("FINDING... COUNT DAY SCHEDULES");
        final String COUNT = "SELECT COUNT(*) FROM day_schedules";
        long count = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info(format("FOUND COUNT %d DAY SCHEDULES"));
        return count;
    }

    @Override
    public void deleteById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("DELETING DAY SCHEDULE BY ID - %d", integer));
        final String DELETE_BY_ID = "DELETE FROM day_schedules WHERE day_schedule_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(DaySchedule.class));
        logger.info(format("DELETED DAY SCHEDULE BY ID - %d", integer));
    }

    @Override
    public void delete(DaySchedule entity) {
        requiredNonNull(entity);
        logger.info(format("DELETING DAY SCHEDULE BY DATE %s", entity.getDate()));
        final String DELETE_BY_DATE = "DELETE FROM day_schedules WHERE date = ?";
        jdbcTemplate.update(DELETE_BY_DATE, new Object[]{entity.getDate()}, new BeanPropertyRowMapper<>(DaySchedule.class));
        logger.info(format("DELETED DAY SCHEDULE BY DATE %s SUCCESSFULLY", entity.getDate()));
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING ALL DAY SCHEDULES");
        final String DELETE_ALL = "DELETE FROM day_schedules";
        jdbcTemplate.update(DELETE_ALL, new BeanPropertyRowMapper<>(DaySchedule.class));
        logger.info("DELETED ALL DAY SCHEDULES SUCCESSFULLY");
    }

    @Override
    public void update(LocalDate newDate, Integer dayId) {
        requiredNonNull(newDate);
        requiredNonNull(dayId);
        logger.info(format("UPDATING DAY SCHEDULE ON NEW DATE - %s BY ID - %d", newDate, dayId));
        final String UPDATE = "UPDATE day_schedules SET date = ? WHERE day_schedule_id = ?";
        jdbcTemplate.update(UPDATE, new Object[]{Date.valueOf(newDate), dayId}, new BeanPropertyRowMapper<>(DaySchedule.class));
        logger.info(format("UPDATED DAY SCHEDULE ON NEW DATE - %s BY ID - %d SUCCESSFULLY", newDate, dayId));
    }

    @Override
    public Optional<DaySchedule> findByDate(LocalDate date) {
        requiredNonNull(date);
        logger.info(format("FINDING DAY SCHEDULE BY DATE %s", date));
        Optional<DaySchedule> daySchedule = Optional.of(findDayScheduleByDate(date))
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find day schedule by date %s", date)));
        daySchedule.get().setLessons(findById(daySchedule.get().getId()).get().getLessons());
        logger.info(format("FOUND DAY SCHEDULE %s BY DATE - %s", daySchedule, date));
        return daySchedule;
    }

    @Override
    public void addLessonToSchedule(Lesson lesson, DaySchedule daySchedule) {
        requiredNonNull(lesson);
        requiredNonNull(daySchedule);
        logger.info(format("ADDING... %s TO %s", lesson, daySchedule));
        final String ADD_LESSON = "INSERT INTO day_schedules_lessons (day_schedule_id, lesson_id) VALUES (?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(ADD_LESSON);
            statement.setInt(1, daySchedule.getId());
            statement.setInt(2, lesson.getId());
            statement.executeUpdate();
            logger.info(format("ADDED %s TO %s SUCCESSFULLY", lesson, daySchedule));
            return statement;
        });
    }

    @Override
    public Optional<DaySchedule> findStudentDaySchedule(Student student, LocalDate date) {
        requiredNonNull(student);
        requiredNonNull(date);
        logger.info(format("FINDING... STUDENT DAY SCHEDULE BY %s AND %s", student, date));
        final String FIND_STUDENT_DAY_SCHEDULE = "SELECT sl.lesson_id  FROM day_schedules_lessons sl JOIN day_schedules ds" +
                "ON (sl.day_schedule_id = ds.day_schedule_id) JOIN lessons l ON (sl.lesson_id = l.lesson_id) " +
                "JOIN students s (l.group_id = s.group_id) WHERE ds.date = ? AND s.student_id = ?";
        Optional<DaySchedule> studentSchedule = Optional.of(findPersonDaySchedule(student, date, FIND_STUDENT_DAY_SCHEDULE))
                .orElseThrow(() -> new IllegalArgumentException("Can't find student day schedule"));
        logger.info(format("FOUND %s STUDENT DAY SCHEDULE BY %s AND %s", studentSchedule, student, date));
        return studentSchedule;
    }

    @Override
    public Optional<DaySchedule> findTeacherDaySchedule(Teacher teacher, LocalDate date) {
        requiredNonNull(teacher);
        requiredNonNull(date);
        logger.info(format("FINDING... TEACHER DAY SCHEDULE BY %s AND %s", teacher, date));
        final String FIND_TEACHER_DAY_SCHEDULE = "SELECT sl.lesson_id FROM day_schedules_lessons sl JOIN day_schedules ds " +
                "ON (sl.day_schedule_id = ds.day_schedule_id) JOIN lessons l ON (sl.lesson_id = l.lesson_id) " +
                "JOIN teachers t ON (l.teacher_id = t.teacher_id) WHERE ds.date = ? AND t.teacher_id = ?";
        Optional<DaySchedule> teacherSchedule = Optional.of(findPersonDaySchedule(teacher, date, FIND_TEACHER_DAY_SCHEDULE))
                .orElseThrow(() -> new IllegalArgumentException("Can't find teacher day schedule"));
        logger.info(format("FOUND %s TEACHER DAY SCHEDULE BY %s AND %s", teacherSchedule, teacher, date));
        return teacherSchedule;
    }

    private Optional<DaySchedule> findPersonDaySchedule(Person person, LocalDate date, String sql) {
        logger.info(format("FINDING... DAY SCHEDULE FOR PERSON - %s BY DATE - %s", person, date));
        Optional<DaySchedule> daySchedule = Optional.of(findDayScheduleByDate(date))
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find day schedule by date - %s", date)));
        List<Lesson> lessonsId = jdbcTemplate.query(sql, new Object[]{Date.valueOf(date),
                person.getId()}, new BeanPropertyRowMapper<>(Lesson.class));
        List<Lesson> dayLessons = new ArrayList<>();
        lessonsId.forEach(lesson -> dayLessons.add(lessonDao.findById(lesson.getId())
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find lesson by id - %d", lesson.getId())))));
        daySchedule.get().setLessons(dayLessons);
        return daySchedule;
    }


    private Optional<DaySchedule> findDayScheduleByDate(LocalDate date) {
        requiredNonNull(date);
        logger.info(format("FINDING... DAY SCHEDULE BY DATE - %s", date));
        final String FIND_DAY_SCHEDULE_BY_DATE = "SELECT * FROM day_schedules WHERE date = ?";
        Optional<DaySchedule> daySchedule = Optional.of(Optional.ofNullable(jdbcTemplate.queryForObject(FIND_DAY_SCHEDULE_BY_DATE,
                        new Object[]{date}, new BeanPropertyRowMapper<>(DaySchedule.class)))
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find day schedule by date - %s", date))));
        logger.info(format("FOUND DAY SCHEDULE %s BY DATE - %s", daySchedule, date));
        return daySchedule;
    }

    private Optional<DaySchedule> findDayScheduleById(Integer id) {
        requiredNonNull(id);
        logger.info(format("FINDING... DAY SCHEDULE BY ID - %d", id));
        final String FIND_DAY_SCHEDULE_BY_ID = "SELECT * FROM day_schedules WHERE day_schedule_id = ?";
        Optional<DaySchedule> daySchedule = Optional.of(Optional.ofNullable(jdbcTemplate.queryForObject(FIND_DAY_SCHEDULE_BY_ID,
                        new Object[]{id}, new BeanPropertyRowMapper<>(DaySchedule.class))))
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find day schedule by id - %d", id)));
        logger.info(format("FOUND DAY SCHEDULE %s BY ID - %d", daySchedule, id));
        return daySchedule;
    }

    private void requiredNonNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException(ExceptionsHandlingConstants.ARGUMENT_IS_NULL);
        }
    }
}
