package ua.foxminded.task10.uml;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ua.foxminded.task10.uml.configuration.PropertyManager;
import ua.foxminded.task10.uml.dao.*;
import ua.foxminded.task10.uml.dao.impl.*;
import ua.foxminded.task10.uml.model.*;
import ua.foxminded.task10.uml.service.*;
import ua.foxminded.task10.uml.service.impl.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static DataSource dataSource() {
        PropertyManager propertyManager = new PropertyManager(PropertyManager.PROPERTIES_FILE);

//        String driver = propertyManager.getProperty(PropertyManager.DRIVER_CLASS_NAME);
        String url = propertyManager.getProperty(PropertyManager.URL);
        String user = propertyManager.getProperty(PropertyManager.USER);
        String password = propertyManager.getProperty(PropertyManager.PASSWORD);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        return dataSource;
    }

    public static void main(String[] args) throws NoSuchMethodException {
        StudentDao studentDao = new StudentDaoImpl(dataSource());
        GroupDao groupDao = new GroupDaoImpl(dataSource());
        TeacherDao teacherDao = new TeacherDaoImpl(dataSource());
        SubjectDao subjectDao = new SubjectDaoImpl(dataSource());
        ClassroomDao classroomDao = new ClassroomDaoImpl(dataSource());

        StudentService studentService = new StudentServiceImpl(studentDao, groupDao);
        GroupService groupService = new GroupServiceImpl(groupDao, studentDao);
        TeacherService teacherService = new TeacherServiceImpl(teacherDao, subjectDao);
        SubjectService subjectService = new SubjectServiceImpl(subjectDao, teacherDao);
        ClassroomService classroomService = new ClassroomServiceImpl(classroomDao);


        List<Student> students = new ArrayList<>();
        students.add(new Student("aff", "asfs", 2));
        students.add(new Student("aff", "asfs", 2));
        students.add(new Student("aff", "asfs", 2));
        students.add(new Student("aff", "asfs", 1));
        students.add(new Student("aff", "asfs", 1));
        students.add(new Student("aff", "asfs", 2));

        List<Group> groups = new ArrayList<>();
        groups.add(new Group("racer"));
        groups.add(new Group("finance"));
        groups.add(new Group("math"));

        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher("Mark", "Holur"));
        teachers.add(new Teacher("Olef", "Sholts"));
        teachers.add(new Teacher("Kirol", "Mings"));
        teachers.add(new Teacher("Dourin", "Faust"));

        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("History"));
        subjects.add(new Subject("Biology"));
        subjects.add(new Subject("Math"));

        List<Classroom> classrooms = new ArrayList<>();
        classrooms.add(new Classroom(78));
        classrooms.add(new Classroom(23));
        classrooms.add(new Classroom(34));
        classrooms.add(new Classroom(54));
        classrooms.add(new Classroom(65));

        classroomService.count();

//
//       List<Subject> subjects1 = subjectService.findAll();

//        subjectService.findTeacherSubjects(8);
//
//        teacherService.addTeacherToSubjects(new Teacher(7), subjects1);



        // studentService.findStudentsByGroupId(6);

//       groupService.save(new Group("chemistry"));

//        studentService.findStudentsByGroupName(groupService.findByGroupName("sport").getId());




//        studentService.saveAll(students);
//studentService.findByCourseNumber(2);



//        groupService.assignStudentsToGroup(s, new Group(8));


    }
}
