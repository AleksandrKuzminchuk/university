package ua.foxminded.task10.uml;
/*

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ua.foxminded.task10.uml.dao.*;
import ua.foxminded.task10.uml.dao.impl.*;
import ua.foxminded.task10.uml.dao.mapper.*;
import ua.foxminded.task10.uml.model.*;
import ua.foxminded.task10.uml.service.*;
import ua.foxminded.task10.uml.service.impl.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final String PROPERTIES_FILE = "src/resources/db.properties";
    public static final String DRIVER = "db.driver";
    public static final String URL = "db.url";
    public static final String USER = "db.user";
    public static final String PASSWORD = "db.password";

    public DataSource dataSource;

    public DataSource dataSource() {
        if (dataSource != null) return dataSource;
//        PropertyManager propertyManager = new PropertyManager(PROPERTIES_FILE);

//        String driver = propertyManager.getProperty(DRIVER);
//        String url = propertyManager.getProperty(URL);
//        String user = propertyManager.getProperty(USER);
//        String password = propertyManager.getProperty(PASSWORD);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(driver);
        dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
        dataSource.setUsername("postgres");
        dataSource.setPassword("1234");

        return dataSource;
    }

    public static void main(String[] args) {
        DataSource dataSource = new Main().dataSource();
        StudentDao studentDao = new StudentDaoImpl(dataSource);
        GroupDao groupDao = new GroupDaoImpl(dataSource);
        TeacherDao teacherDao = new TeacherDaoImpl(dataSource);
        SubjectDao subjectDao = new SubjectDaoImpl(dataSource);
        ClassroomDao classroomDao = new ClassroomDaoImpl(dataSource);
        EventDao eventDao = new EventDaoImpl(dataSource, new EventRowMapper(new SubjectRowMapper(), new ClassroomRowMapper(),
                new TeacherRowMapper(), new GroupRowMapper()));


        GroupService groupService = new GroupServiceImpl(groupDao, studentDao);
        StudentService studentService = new StudentServiceImpl(studentDao, groupDao);
        TeacherService teacherService = new TeacherServiceImpl(teacherDao, subjectDao);
        SubjectService subjectService = new SubjectServiceImpl(subjectDao, teacherDao);
        ClassroomService classroomService = new ClassroomServiceImpl(classroomDao);
        EventService eventService = new EventServiceImpl(eventDao, teacherDao, groupDao, subjectDao, classroomDao);


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

//subjectService.deleteAll();

//        eventService.findAll();
        // classroomService.count();

//
//       List<Subject> subjects1 = subjectService.findAll();

//        subjectService.findTeacherSubjects(8);
//
//        teacherService.addTeacherToSubjects(new Teacher(7), subjects1);


//         studentService.deleteStudentsByCourseNumber(3);

//       groupService.assignStudentToGroup(new Student(43), new Group(5));

//        studentService.findStudentsByGroupName(groupService.findByGroupName("sport").getId());


//        studentService.saveAll(students);
//studentService.findAll();


//        groupService.assignStudentsToGroup(s, new Group(8));
    }
}


*/
