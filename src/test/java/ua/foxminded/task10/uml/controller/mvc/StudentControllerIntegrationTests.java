package ua.foxminded.task10.uml.controller.mvc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.dto.StudentDTO;
import ua.foxminded.task10.uml.dto.response.StudentUpdateResponse;
import ua.foxminded.task10.uml.model.Course;
import ua.foxminded.task10.uml.model.Person;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.task10.uml.util.ConstantsTests.*;
import static ua.foxminded.task10.uml.util.MediaTypeTest.MEDIA_TYPE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(value = {"classpath:create-table-students.sql", "classpath:create-table-groups.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StudentControllerIntegrationTests {

    @Autowired
    private StudentService service;
    @Autowired
    private GroupService groupService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        service.deleteAll();
        groupService.deleteAll();
    }

    @AfterEach
    void tearDown() {
        service.deleteAll();
        groupService.deleteAll();
    }

    @Test
    void shouldReturnStudentsDTOList() throws Exception {

        List<StudentDTO> studentsDTO = createStudentsDTO();

        ResultActions response = mockMvc.perform(get("/students"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("students", studentsDTO))
                .andExpect(model().attribute("students", hasSize(2)))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("firstName", is("Mark")),
                                hasProperty("lastName", is("Oliver"))
                        )
                )))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                                hasProperty("id", is(2)),
                                hasProperty("firstName", is("Mark")),
                                hasProperty("lastName", is("Humek"))
                        )
                )))
                .andExpect(model().attribute("count", is(2)))
                .andExpect(view().name("students/students"));
    }

    @Test
    void shouldReturnSaveFormStudentDTO() throws Exception {

        ResultActions response = mockMvc.perform(get("/students/new")
                .sessionAttr("newStudent", StudentDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(view().name("students/formSaveStudent"));
    }

    @Test
    void shouldReturnSavedStudentDTO_whenSave() throws Exception {

        StudentDTO saveStudent = new StudentDTO("Mark", "Loren", 5);

        ResultActions response = mockMvc.perform(post("/students/saved")
                .contentType(MEDIA_TYPE)
                .param("firstName", saveStudent.getFirstName())
                .param("lastName", saveStudent.getLastName())
                .param("course", String.valueOf(saveStudent.getCourse()))
                .sessionAttr("newStudent", StudentDTO.class));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("student", hasProperty("id", is(1))))
                .andExpect(model().attribute("student", hasProperty("firstName", is(saveStudent.getFirstName()))))
                .andExpect(model().attribute("student", hasProperty("lastName", is(saveStudent.getLastName()))))
                .andExpect(model().attribute("student", hasProperty("course", is(saveStudent.getCourse()))))
                .andExpect(view().name("students/formSavedStudent"));
    }

    @Test
    void shouldReturn400NotValid_whenSave() throws Exception {

        StudentDTO saveStudent = new StudentDTO("mark", "loren", 6);

        ResultActions response = mockMvc.perform(post("/students/saved")
                .contentType(MEDIA_TYPE)
                .param("firstName", saveStudent.getFirstName())
                .param("lastName", saveStudent.getLastName())
                .param("course", String.valueOf(saveStudent.getCourse()))
                .sessionAttr("newStudent", StudentDTO.class));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUpdateFormWithFullFieldsStudentWithId() throws Exception {

        StudentUpdateResponse studentUpdateResponse = StudentUpdateResponse.builder().student(createStudentDTO()).groups(createGroupsDTO()).build();

        ResultActions response = mockMvc.perform(get("/students/{id}/update", studentUpdateResponse.getStudent().getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(xpath("//*[@id=\"studentId\"][@value=1]").exists())
                .andExpect(xpath("//*[@id=\"groupId\"][@value=1]").exists())
                .andExpect(view().name("students/formUpdateStudent"));
    }

    @Test
    void shouldReturnUpdatedStudentDTO_whenUpdate() throws Exception {

        GroupDTO groupDTO = createGroupDTO();

        StudentDTO updateStudentDTO = createStudentDTO();

        ResultActions response = mockMvc.perform(patch("/students/{id}/updated", updateStudentDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("firstName", "Kiril")
                .param("lastName", "Hurmek")
                .param("course", "1")
                .param("group.id", String.valueOf(groupDTO.getId()))
                .param("group.name", String.valueOf(groupDTO.getName()))
                .sessionAttr("updateStudent", StudentDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("updateStudent", hasProperty("id", is(updateStudentDTO.getId()))))
                .andExpect(model().attribute("updateStudent", hasProperty("firstName", is("Kiril"))))
                .andExpect(model().attribute("updateStudent", hasProperty("lastName", is("Hurmek"))))
                .andExpect(model().attribute("updateStudent", hasProperty("course", is(1))))
                .andExpect(model().attribute("updateStudent", hasProperty("group", is(groupDTO))))
                .andExpect(view().name("students/formUpdatedStudent"));
    }

    @Test
    void shouldReturnUpdatedStudentDTO_whenChangeGroup() throws Exception {

        GroupDTO groupDTO = createGroupDTO();
        GroupDTO newGroupDTO = createGroupDTOForUpdate();

        StudentDTO updateStudentDTO = createStudentDTO();
        updateStudentDTO.setGroup(groupDTO);

        service.update(updateStudentDTO);

        ResultActions response = mockMvc.perform(patch("/students/{id}/updated", updateStudentDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("firstName", "Kiril")
                .param("lastName", "Hurmek")
                .param("course", "1")
                .param("group.id", String.valueOf(newGroupDTO.getId()))
                .param("group.name", String.valueOf(newGroupDTO.getName()))
                .sessionAttr("updateStudent", StudentDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("updateStudent", hasProperty("id", is(updateStudentDTO.getId()))))
                .andExpect(model().attribute("updateStudent", hasProperty("firstName", is("Kiril"))))
                .andExpect(model().attribute("updateStudent", hasProperty("lastName", is("Hurmek"))))
                .andExpect(model().attribute("updateStudent", hasProperty("course", is(1))))
                .andExpect(model().attribute("updateStudent", hasProperty("group", is(newGroupDTO))))
                .andExpect(view().name("students/formUpdatedStudent"));
    }

    @Test
    void shouldReturn400NotValid_whenUpdate() throws Exception {

        StudentDTO updateStudentDTO = createStudentDTO();

        ResultActions response = mockMvc.perform(patch("/students/{id}/updated", updateStudentDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("firstName", "kiril")
                .param("lastName", "hurmek")
                .param("course", "7")
                .sessionAttr("updateStudent", StudentDTO.class));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404NotFoundStudent_whenUpdate() throws Exception {

        ResultActions response = mockMvc.perform(patch("/students/{id}/updated", ID_NOT_EXISTS)
                .contentType(MEDIA_TYPE)
                .param("firstName", "Kiril")
                .param("lastName", "Hurmek")
                .param("course", "1")
                .sessionAttr("updateStudent", StudentDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundGroup_whenUpdate() throws Exception {

        StudentDTO updateStudentDTO = createStudentDTO();

        ResultActions response = mockMvc.perform(patch("/students/{id}/updated", updateStudentDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("firstName", "Kiril")
                .param("lastName", "Hurmek")
                .param("course", "1")
                .param("group.id", String.valueOf(ID_NOT_EXISTS))
                .sessionAttr("updateStudent", StudentDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200_whenDeleteById() throws Exception {

        StudentDTO deleteStudent = createStudentDTO();

        ResultActions response = mockMvc.perform(delete("/students/{id}/deleted", deleteStudent.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("students/formDeletedStudent"));
    }

    @Test
    void shouldReturn404NotFound_whenDeleteById() throws Exception {

        ResultActions response = mockMvc.perform(delete("/students/{id}/deleted", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200whenDeleteFromGroup() throws Exception {

        GroupDTO groupDTO = createGroupDTO();

        StudentDTO deleteStudentFromGroup = createStudentDTO();
        deleteStudentFromGroup.setGroup(groupDTO);

        service.update(deleteStudentFromGroup);

        ResultActions response = mockMvc.perform(patch("/students/{id}/delete/from_group", deleteStudentFromGroup.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("students/formDeletedTheStudents'Group"));
    }

    @Test
    void shouldReturn200_whenDeleteAllByGroupId() throws Exception {

        GroupDTO groupDTO = createGroupDTO();

        List<StudentDTO> studentsDTO = createStudentsDTO();

        studentsDTO.stream().peek(studentDTO -> studentDTO.setGroup(groupDTO)).forEach(studentDTO -> service.update(studentDTO));

        ResultActions response = mockMvc.perform(delete("/students/delete/all/by_group/{groupId}", groupDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("students/deletedAllStudentsByGroupId"));
    }

    @Test
    void shouldReturnFindByCourseForm() throws Exception {

        ResultActions response = mockMvc.perform(get("/students/find/by_course")
                .contentType(MEDIA_TYPE)
                .sessionAttr("course", Course.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("students/formFindStudentsByCourse"));
    }

    @Test
    void shouldReturnStudentsDTOList_whenFindByCourse() throws Exception {

        List<StudentDTO> studentsDTO = createStudentsDTO().stream()
                .sorted((x, y) -> x.getLastName().compareTo(y.getLastName())).collect(Collectors.toList());

        Course course = new Course();
        course.setCourse(5);

        ResultActions response = mockMvc.perform(get("/students/found/by_course")
                .contentType(MEDIA_TYPE)
                .param("course", String.valueOf(course.getCourse()))
                .sessionAttr("course", Course.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                                hasProperty("id", is(studentsDTO.get(0).getId())),
                                hasProperty("firstName", is(studentsDTO.get(0).getFirstName())),
                                hasProperty("lastName", is(studentsDTO.get(0).getLastName())),
                                hasProperty("course", is(studentsDTO.get(0).getCourse()))
                        )
                )))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                                hasProperty("id", is(studentsDTO.get(1).getId())),
                                hasProperty("firstName", is(studentsDTO.get(1).getFirstName())),
                                hasProperty("lastName", is(studentsDTO.get(1).getLastName())),
                                hasProperty("course", is(studentsDTO.get(1).getCourse()))
                        )
                )))
                .andExpect(model().attribute("count", is(studentsDTO.size())))
                .andExpect(model().attribute("courseNumber", is(course.getCourse())))
                .andExpect(view().name("students/students"));
    }

    @Test
    void shouldReturn400NotValid_whenFindByCourse() throws Exception {

        Course course = new Course();
        course.setCourse(6);

        ResultActions response = mockMvc.perform(get("/students/found/by_course")
                .contentType(MEDIA_TYPE)
                .param("course", String.valueOf(course.getCourse()))
                .sessionAttr("course", Course.class));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200_whenDeleteAllCourse() throws Exception {

        Integer course = 5;

        createStudentsDTO();

        ResultActions response = mockMvc.perform(delete("/students/delete/by_course/{course}", course));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("courseNumber", is(course)))
                .andExpect(view().name("students/formDeletedStudentsByCourseNumber"));
    }

    @Test
    void shouldReturnFindByGroupNameForm() throws Exception {

        createGroupsDTO();

        ResultActions response = mockMvc.perform(get("/students/find/by_group")
                .sessionAttr("student", StudentDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("groups", hasSize(1)))
                .andExpect(view().name("students/formForFindStudentsByGroupName"));
    }

    @Test
    void shouldReturnStudentsListByGroupName_whenFindByGroupName() throws Exception {

        GroupDTO groupDTO = createGroupDTO();

        List<StudentDTO> studentsDTO = createStudentsDTO();
        studentsDTO.stream().peek(studentDTO -> studentDTO.setGroup(groupDTO))
                .forEach(studentDTO -> service.update(studentDTO));

        ResultActions response = mockMvc.perform(get("/students/found/by_group")
                .contentType(MEDIA_TYPE)
                        .param("id", String.valueOf(groupDTO.getId()))
                        .param("name", String.valueOf(groupDTO.getName()))
                .sessionAttr("findGroup", GroupDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("students", studentsDTO))
                .andExpect(model().attribute("students", hasSize(2)))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("firstName", is("Mark")),
                                hasProperty("lastName", is("Oliver")),
                                hasProperty("group", is(groupDTO))
                        )
                )))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                                hasProperty("id", is(2)),
                                hasProperty("firstName", is("Mark")),
                                hasProperty("lastName", is("Humek")),
                                hasProperty("group", is(groupDTO))
                        )
                )))
                .andExpect(model().attribute("count", studentsDTO.size()))
                .andExpect(model().attribute("groupId", is(groupDTO.getId())))
                .andExpect(model().attribute("group", is(groupDTO)))
                .andExpect(view().name("students/students"));
    }

    @Test
    void shouldReturnStudentsDTOListByGroupId_whenFindBuGroupId() throws Exception {

        GroupDTO groupDTO = createGroupDTO();

        List<StudentDTO> studentsDTO = createStudentsDTO();
        studentsDTO.stream().peek(studentDTO -> studentDTO.setGroup(groupDTO))
                .forEach(studentDTO -> service.update(studentDTO));

        ResultActions response = mockMvc.perform(get("/students/found/by_group/{groupId}", groupDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("students", studentsDTO))
                .andExpect(model().attribute("students", hasSize(2)))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("firstName", is("Mark")),
                                hasProperty("lastName", is("Oliver")),
                                hasProperty("group", is(groupDTO))
                        )
                )))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                                hasProperty("id", is(2)),
                                hasProperty("firstName", is("Mark")),
                                hasProperty("lastName", is("Humek")),
                                hasProperty("group", is(groupDTO))
                        )
                )))
                .andExpect(model().attribute("count", studentsDTO.size()))
                .andExpect(model().attribute("group", is(groupDTO.getId())))
                .andExpect(view().name("students/students"));
    }

    @Test
    void shouldReturn404NotFound_whenFindBuGroupId() throws Exception {

        ResultActions response = mockMvc.perform(get("/students/found/by_group/{groupId}", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnFormForFindByNameOrSurname() throws Exception {

        ResultActions response = mockMvc.perform(get("/students/find/by_name_surname")
                .sessionAttr("newStudent", Person.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("students/formFindStudentByNameSurname"));
    }

    @Test
    void shouldReturnStudentsDTOList_whenFindByNameOrSurname() throws Exception {

        createStudentsDTO();

        ResultActions response = mockMvc.perform(get("/students/found/by_name_surname")
                .contentType(MEDIA_TYPE)
                .param("firstName", "Mark")
                .param("lastName", "Gepart")
                .sessionAttr("findStudent", Person.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("students", hasSize(2)))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("firstName", is("Mark")),
                                hasProperty("lastName", is("Oliver"))
                        )
                )))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                                hasProperty("id", is(2)),
                                hasProperty("firstName", is("Mark")),
                                hasProperty("lastName", is("Humek"))
                        )
                )))
                .andExpect(view().name("students/students"));
    }

    @Test
    void shouldReturn200_whenDeleteAll() throws Exception {

        createStudentsDTO();

        ResultActions response = mockMvc.perform(delete("/students/deleted/all"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("students/formDeleteAllStudents"));
    }

    private List<StudentDTO> createStudentsDTO() {
        List<StudentDTO> studentDTOS = new ArrayList<>();
        studentDTOS.add(new StudentDTO("Mark", "Oliver", 5));
        studentDTOS.add(new StudentDTO("Mark", "Humek", 5));
        return studentDTOS.stream().
                map(studentDTO1 -> service.save(studentDTO1)).collect(Collectors.toList());
    }

    private StudentDTO createStudentDTO() {
        return service.save(new StudentDTO("Mark", "Oliver", 5));
    }

    private GroupDTO createGroupDTO() {
        return groupService.save(new GroupDTO("G-10"));
    }

    private GroupDTO createGroupDTOForUpdate() {
        return groupService.save(new GroupDTO("G-15"));
    }

    private List<GroupDTO> createGroupsDTO() {
        List<GroupDTO> groupsDTO = new ArrayList<>();
        GroupDTO groupDTOTest = new GroupDTO();
        groupDTOTest.setName("G-10");
        groupsDTO.add(groupDTOTest);
        return groupsDTO.stream().map(groupDTO -> groupService.save(groupDTO)).collect(Collectors.toList());
    }
}