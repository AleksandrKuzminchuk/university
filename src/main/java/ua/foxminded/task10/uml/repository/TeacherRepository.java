package ua.foxminded.task10.uml.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.task10.uml.model.Teacher;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    List<Teacher> findByFirstNameOrLastName(String firstName, String lastName, Sort name);
    @Query(value = "SELECT case when count(*) > 0 then true else false end FROM teachers_subjects ts " +
            "WHERE  ts.teacher_id = ?1 AND ts.subject_id = ?2", nativeQuery = true)
    boolean existsSubjectAndTeacher(Integer teacherId, Integer subjectId);
}
