package ua.foxminded.task10.uml.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.dto.StudentCreateDTO;
import ua.foxminded.task10.uml.dto.StudentDTO;
import ua.foxminded.task10.uml.dto.StudentUpdateDTO;
import ua.foxminded.task10.uml.dto.mapper.StudentMapper;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.task10.uml.util.ConstantsTests.ID_NOT_EXISTS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(value = {"classpath:create-table-students.sql", "classpath:create-table-groups.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StudentRestControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StudentService studentService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        studentService.deleteAll();
        groupService.deleteAll();
    }

    @AfterEach
    void tearDown() {
        studentService.deleteAll();
        groupService.deleteAll();
    }

    @Test
    void givenListOfStudentsDTO_whenGetAll_thenReturnStudentsDTOList() throws Exception {

        createStudentsDTO();

        ResultActions response = mockMvc.perform(get("/api/students"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.students.size()", is(2)));

    }

    @Test
    void givenStudentDTOObject_whenCreate_thenReturnSavedStudentDTO() throws Exception{

        StudentCreateDTO studentDTO = new StudentCreateDTO("Mark", "Oliver", 5);

        ResultActions response = mockMvc.perform(post("/api/students/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is(studentDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(studentDTO.getLastName())))
                .andExpect(jsonPath("$.course", is(studentDTO.getCourse())));
    }

    @Test
    void givenStudentDTOObject_whenCreate_thenReturnBadRequest400NotValid() throws Exception{

        StudentCreateDTO studentDTO = new StudentCreateDTO("mark", "Oliver", 6);

        ResultActions response = mockMvc.perform(post("/api/students/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenUpdatedStudentDTO_whenUpdate_thenReturnUpdatedStudentDTOObject() throws Exception{

        GroupDTO group = createGroupDTO();

        StudentDTO studentDTO = createStudentDTO();

        StudentUpdateDTO updatedStudent = new StudentUpdateDTO();
        updatedStudent.setFirstName("Kiril");
        updatedStudent.setLastName("Hurmek");
        updatedStudent.setCourse(4);
        updatedStudent.setGroup(group);

        ResultActions response = mockMvc.perform(patch("/api/students/update/{id}", studentDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(studentDTO.getId())))
                .andExpect(jsonPath("$.firstName", is(updatedStudent.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedStudent.getLastName())))
                .andExpect(jsonPath("$.course", is(updatedStudent.getCourse())))
                .andExpect(jsonPath("$.group.id", is(updatedStudent.getGroup().getId())))
                .andExpect(jsonPath("$.group.name", is(updatedStudent.getGroup().getName())));
    }

    @Test
    void givenUpdatedStudentDTO_whenUpdate_thenReturnNotFound404StudentNotExists() throws Exception {

        createStudentDTO();

        StudentUpdateDTO updatedStudent = new StudentUpdateDTO();
        updatedStudent.setFirstName("Kiril");
        updatedStudent.setLastName("Hurmek");
        updatedStudent.setCourse(4);

        ResultActions response = mockMvc.perform(patch("/api/students/update/{id}", ID_NOT_EXISTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)));

        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    void givenUpdatedStudentDTO_whenUpdate_thenReturn404NotFoundGroupNotExists() throws Exception{

        StudentDTO studentDTO = createStudentDTO();

        StudentUpdateDTO updatedStudent = new StudentUpdateDTO();
        updatedStudent.setFirstName("Kiril");
        updatedStudent.setLastName("Hurmek");
        updatedStudent.setCourse(4);
        updatedStudent.setGroup(new GroupDTO(6));

        ResultActions response = mockMvc.perform(patch("/api/students/update/{id}", studentDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void givenUpdatedStudentDTO_whenUpdate_thenReturnBadRequest400NotValid() throws Exception {

        StudentDTO studentDTO = createStudentDTO();

        StudentUpdateDTO updatedStudent = new StudentUpdateDTO();
        updatedStudent.setFirstName("kiril");
        updatedStudent.setLastName("hurmek");
        updatedStudent.setCourse(8);

        ResultActions response = mockMvc.perform(patch("/api/students/update/{id}", studentDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)));

        response.andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    void givenResponseEntity_whenDeleteById_thenReturn200() throws Exception {

        StudentDTO studentDTO = createStudentDTO();

        ResultActions response = mockMvc.perform(delete("/api/students/{id}/delete", studentDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenResponseEntity_whenDeleteById_thenReturn404NotFound() throws Exception {

        ResultActions response = mockMvc.perform(delete("/api/students/{id}/delete", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenStudentId_whenDeleteGroup_thenReturn200() throws Exception {

        GroupDTO group = createGroupDTO();

        StudentDTO student = createStudentDTO();
        student.setGroup(group);
        studentService.update(student);

        ResultActions response = mockMvc.perform(patch("/api/students/{id}/delete/from_group", student.getId()));

        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void givenStudentId_whenDeleteGroup_thenReturnNotFound404() throws Exception {

        GroupDTO group = createGroupDTO();

        StudentDTO student = createStudentDTO();
        student.setGroup(group);
        studentService.update(student);

        ResultActions response = mockMvc.perform(patch("/api/students/{id}/delete/from_group", ID_NOT_EXISTS));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void givenResponseEntity_whenDeleteAllByGroup_thenReturn200() throws Exception {

        GroupDTO group = createGroupDTO();

        createStudentsDTO().stream().peek(studentDTO1 -> studentDTO1.setGroup(group)).
                forEach(studentDTO1 -> studentService.update(studentDTO1));

        ResultActions response = mockMvc.perform(delete("/api/students/delete/all/by_group/{id}", group.getId()));

        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void givenResponseEntity_whenDeleteAllByGroup_thenReturnNotFound404() throws Exception {

        GroupDTO group = createGroupDTO();

        createStudentsDTO().stream().peek(studentDTO1 -> studentDTO1.setGroup(group)).
                forEach(studentDTO1 -> studentService.update(studentDTO1));

        ResultActions response = mockMvc.perform(delete("/api/students/delete/all/by_group/{id}", ID_NOT_EXISTS));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void givenResponseEntity_whenFindByCourse_thenReturn200() throws Exception {

        createStudentsDTO();

        ResultActions response = mockMvc.perform(get("/api/students/find/by_course/5"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.students.size()", is(2)));
    }

    @Test
    void givenResponseEntity_whenDeleteAllByCourse_thenReturn200() throws Exception {

        createStudentsDTO();

        ResultActions response = mockMvc.perform(delete("/api/students/delete/by_course/5"));

        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void givenStudentsDTO_whenFindByGroup_thenReturnStudentsDTOList() throws Exception {

        GroupDTO group = createGroupDTO();

        createStudentsDTO().stream().peek(studentDTO1 -> studentDTO1.setGroup(group))
                .forEach(studentDTO1 -> studentService.update(studentDTO1));

        ResultActions response = mockMvc.perform(get("/api/students/find/by_group/{id}", group.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.students.size()", is(2)));
    }

    @Test
    void givenStudentsDTO_whenFindByGroup_thenReturnNotFound404() throws Exception {

        ResultActions response = mockMvc.perform(get("/api/students/find/by_group/{id}", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenStudentsDTO_whenFindByFirstNameOrLastName_thenReturnStudentsDTOList() throws Exception {

       createStudentsDTO();

        HttpHeaders headers = new HttpHeaders();
        headers.set("firstName", "Mark");
        headers.set("lastName", "Loren");

        ResultActions response = mockMvc.perform(get("/api/students/find/name-or-surname")
                .headers(headers));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.students.size()", is(2)));
    }

    @Test
    void givenResponseEntity_whenDeleteAll_thenReturn200() throws Exception {

        createStudentsDTO();

        ResultActions response = mockMvc.perform(delete("/api/students/delete/all"));

        response.andDo(print())
                .andExpect(status().isOk());
    }

    private List<StudentDTO> createStudentsDTO(){
        List<StudentDTO> studentDTOS = new ArrayList<>();
        studentDTOS.add(new StudentDTO("Mark", "Oliver", 5));
        studentDTOS.add(new StudentDTO("Mark", "Humek", 5));
        return studentDTOS.stream().
                map(studentDTO1 -> studentService.save(studentDTO1)).collect(Collectors.toList());
    }

    private StudentDTO createStudentDTO(){
        return studentService.save(new StudentDTO("Mark", "Oliver", 5));
    }

    private GroupDTO createGroupDTO(){
        return groupService.save(new GroupDTO("G-10"));
    }
}