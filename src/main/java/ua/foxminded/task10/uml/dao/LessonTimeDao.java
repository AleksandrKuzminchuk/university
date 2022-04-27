package ua.foxminded.task10.uml.dao;

import ua.foxminded.task10.uml.model.curriculums.Lesson;
import ua.foxminded.task10.uml.model.curriculums.LessonTime;

import java.util.List;

public interface LessonTimeDao extends CrudRepository<LessonTime, Integer> {

    void saveAll(List<LessonTime> lessonTimes);

    void updateLessonTime(Integer lessonTimeId);
}
