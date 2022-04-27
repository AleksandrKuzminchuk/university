package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.curriculums.Lesson;

import java.util.List;
import java.util.Optional;

public interface LessonDao extends CrudRepository<Lesson, Integer>{

    void saveAll(List<Lesson> lessons);

    void updateLesson(Integer subjectId, Integer classroomId, Integer teacherId, Integer groupId, Integer lessonTimeId, Integer lessonId);
}
