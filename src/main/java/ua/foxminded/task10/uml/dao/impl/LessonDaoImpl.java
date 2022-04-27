package ua.foxminded.task10.uml.dao.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.foxminded.task10.uml.dao.LessonDao;
import ua.foxminded.task10.uml.dao.LessonTimeDao;
import ua.foxminded.task10.uml.dao.TeacherDao;
import ua.foxminded.task10.uml.exceptions.ExceptionsHandlingConstants;
import ua.foxminded.task10.uml.model.curriculums.Lesson;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class LessonDaoImpl implements LessonDao {

    private final static Logger logger = Logger.getLogger(LessonDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final LessonTimeDao lessonTimeDao;
    private final SubjectDaoImpl subjectDao;
    private final ClassroomDaoImpl classroomDao;
    private final GroupDaoImpl groupDao;
    private final TeacherDao teacherDao;

    public LessonDaoImpl(DataSource dataSource, LessonTimeDao lessonTimeDao, SubjectDaoImpl subjectDao,
                         ClassroomDaoImpl classroomDao, GroupDaoImpl groupDao, TeacherDao teacherDao) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.lessonTimeDao = lessonTimeDao;
        this.subjectDao = subjectDao;
        this.classroomDao = classroomDao;
        this.groupDao = groupDao;
        this.teacherDao = teacherDao;
    }

    @Override
    public Optional<Lesson> save(Lesson entity) {
        requiredNonNull(entity);
        logger.info(format("SAVING... %s", entity));
        final String SAVE_LESSON = "INSERT INTO lessons (subject_id, classroom_id, teacher_id, group_id, lesson_time_id)" +
                " VALUES(?,?,?,?,?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SAVE_LESSON, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entity.getSubject().getId());
            statement.setInt(2, entity.getClassroom().getId());
            statement.setInt(3, entity.getTeacher().getId());
            statement.setInt(4,  entity.getGroup().getId());
            statement.setInt(5, entity.getLessonTime().getId());
            return statement;
        }, holder);
        Integer lessonId = holder.getKey().intValue();
        entity.setId(lessonId);
        Optional<Lesson> result = Optional.of(entity);
        logger.info(format("SAVED %s SUCCESSFULLY", result));
        return result;
    }

    @Override
    public Optional<Lesson> findById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("FINDING... LESSON BY ID - %d", integer));
        final String FIND_BY_ID = "SELECT * FROM lessons WHERE lesson_id = ?";
        Optional<Lesson> lesson = Optional.of(Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID,
                new Object[]{integer}, new BeanPropertyRowMapper<>(Lesson.class))))
                .orElseThrow(() -> new IllegalArgumentException(format("Can't find lesson by id - %d", integer)));
        logger.info(format("FOUND %s BY ID - %d", lesson, integer));
        return lesson;
    }

    @Override
    public boolean existsById(Integer integer) {

        throw new NotImplementedException("Method existsById not implemented");
    }

    @Override
    public List<Lesson> findAll() {
        logger.info("FINDING... ALL LESSONS");
        final String FIND_ALL = "SELECT * FROM lessons";
        List<Lesson> lessons = jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Lesson.class));
        logger.info(format("FOUND ALL LESSONS - %d", lessons.size()));
        return lessons;
    }

    @Override
    public long count() {
        logger.info("FINDING COUNT LESSONS");
        final String COUNT = "SELECT COUNT(*) FROM lessons";
        long count = jdbcTemplate.queryForObject(COUNT, Long.class);
        logger.info(format("FOUND COUNT - %d SUCCESSFULLY", count));
        return count;
    }

    @Override
    public void deleteById(Integer integer) {
        requiredNonNull(integer);
        logger.info(format("DELETING LESSON BY ID - %d", integer));
        final String DELETE_BY_ID = "DELETE FROM lessons WHERE lesson_id = ?";
        jdbcTemplate.update(DELETE_BY_ID, new Object[]{integer}, new BeanPropertyRowMapper<>(Lesson.class));
        logger.info(format("DELETED LESSON BY ID - %d SUCCESSFULLY", integer));
    }

    @Override
    public void saveAll(List<Lesson> lessons) {
        requiredNonNull(lessons);
        logger.info(format("SAVING... LESSONS - %d", lessons.size()));
        lessons.forEach(this::save);
        logger.info(format("SAVED LESSONS - %d SUCCESSFULLY", lessons.size()));
    }

    @Override
    public void delete(Lesson entity) {
        throw new NotImplementedException("Method delete not implemented");
    }

    @Override
    public void deleteAll() {
        logger.info("DELETING ALL LESSONS");
        final String DELETE_ALL = "DELETE FROM lessons";
        jdbcTemplate.update(DELETE_ALL, new BeanPropertyRowMapper<>(Lesson.class));
        logger.info("DELETED ALL LESSONS SUCCESSFULLY");
    }

    @Override
    public void updateLesson(Integer subjectId, Integer classroomId, Integer teacherId,
                             Integer groupId, Integer lessonTimeId, Integer lessonId){
        requiredNonNull(subjectId);
        requiredNonNull(classroomId);
        requiredNonNull(teacherId);
        requiredNonNull(groupId);
        requiredNonNull(lessonTimeId);
        requiredNonNull(lessonId);
        logger.info(format("UPDATING LESSON - %d, %d, %d, %d, %d BY ID - %d",
                subjectId, classroomId, teacherId, groupId, lessonTimeId, lessonId));
        final String UPDATE_LESSON = "UPDATE lessons SET subject_id = ?, classroom_id = ?, teacher_id = ?, " +
                "group_id = ?, lesson_time_id WHERE lesson_id = ?";
        jdbcTemplate.update(UPDATE_LESSON, new Object[]{subjectId, classroomId, teacherId, groupId, lessonId, lessonId},
                new BeanPropertyRowMapper<>(Lesson.class));
        logger.info(format("UPDATED LESSON - %d, %d, %d, %d, %d BY ID - %d SUCCESSFULLY",
                subjectId, classroomId, teacherId, groupId, lessonTimeId, lessonId));

    }

    private void requiredNonNull(Object o){
        if (o == null){
            throw new IllegalArgumentException(ExceptionsHandlingConstants.ARGUMENT_IS_NULL);
        }
    }
}
