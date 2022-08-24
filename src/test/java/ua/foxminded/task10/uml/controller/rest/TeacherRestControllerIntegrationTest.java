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
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherCreateDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;
import ua.foxminded.task10.uml.service.SubjectService;
import ua.foxminded.task10.uml.service.TeacherService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.task10.uml.util.ConstantsTests.ID_NOT_EXISTS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(value = {"classpath:create-table-classrooms.sql", "classpath:create-table-subjects.sql", "classpath:create-table-teachers_subjects.sql",
        "classpath:create-table-teachers.sql", "classpath:create-table-groups.sql", "classpath:create-table-events.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TeacherRestControllerIntegrationTest {

    @Autowired
    private TeacherService service;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        subjectService.deleteAll();
        service.deleteAll();
    }

    @AfterEach
    void tearDown() {
        subjectService.deleteAll();
        service.deleteAll();
    }

    @Test
    void givenTeachersDTOList_whenFindAll_thenReturnTeachersDTOList() throws Exception {

        createTeachersDTO();

        ResultActions response = mockMvc.perform(get("/api/teachers"));

        response.andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teachers.size()", is(2)));
    }

    @Test
    void givenTeacherDTOObject_whenCreate_thenReturnTeacherDTOObject() throws Exception {

        TeacherCreateDTO teacherCreateDTO = createTeacherCreateDTO();

        ResultActions response = mockMvc.perform(post("/api/teachers/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(teacherCreateDTO)));

        response.andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(teacherCreateDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(teacherCreateDTO.getLastName())));
    }

    @Test
    void givenTeacherDTOObject_whenCreate_thenReturn400NotValid() throws Exception {

        TeacherCreateDTO teacherCreateDTO = new TeacherCreateDTO("hurmek", "juril");

        ResultActions response = mockMvc.perform(post("/api/teachers/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(teacherCreateDTO)));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenResponseEntity_whenDeleteById_thenReturn200() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        ResultActions response = mockMvc.perform(delete("/api/teachers/{id}/delete", teacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenResponseEntity_whenDeleteById_thenReturn404NotFound() throws Exception {

        ResultActions response = mockMvc.perform(delete("/api/teachers/{id}/delete", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUpdatedTeacherDTO_whenUpdate_thenReturnUpdatedTeacherDTO() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        TeacherCreateDTO updateTeacher = createTeacherCreateDTO();

        ResultActions response = mockMvc.perform(patch("/api/teachers/update/{id}", teacherDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateTeacher)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(teacherDTO.getId())))
                .andExpect(jsonPath("$.firstName", is(updateTeacher.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updateTeacher.getLastName())));
    }

    @Test
    void givenUpdatedTeacherDTO_whenUpdate_thenReturn400NotValid() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        TeacherCreateDTO updateTeacher = new TeacherCreateDTO("hurmek", "juril");

        ResultActions response = mockMvc.perform(patch("/api/teachers/update/{id}", teacherDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateTeacher)));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenUpdatedTeacherDTO_whenUpdate_thenReturn404NotFound() throws Exception {

        TeacherCreateDTO updateTeacher = createTeacherCreateDTO();

        ResultActions response = mockMvc.perform(patch("/api/teachers/update/{id}", ID_NOT_EXISTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateTeacher)));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenResponseEntity_whenDeleteAll_thenReturn200() throws Exception {

        createTeachersDTO();

        ResultActions response = mockMvc.perform(delete("/api/teachers/delete/all"));

        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenTeachersDTOList_whenFindByNameOrSurname_thenReturnTeachersDTOList() throws Exception {

        createTeachersDTO();

        HttpHeaders headers = new HttpHeaders();
        headers.set("firstName", "Hurmek");
        headers.set("lastName", "Markovich");

        ResultActions response = mockMvc.perform(get("/api/teachers/find/name-or-surname")
                .headers(headers));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.teachers.size()", is(2)));
    }

    @Test
    void givenSubjectsDTOList_whenFindSubjects_thenReturnSubjectDTOList() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        List<SubjectDTO> subjectsDTO = createSubjectsDTO();

        subjectsDTO.forEach(subjectDTO -> service.addSubject(teacherDTO.getId(), subjectDTO.getId()));

        ResultActions response = mockMvc.perform(get("/api/teachers/{id}/find/subjects", teacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.subjects.size()", is(2)));
    }

    @Test
    void givenSubjectsDTOList_whenFindSubjects_thenReturn404NotFound() throws Exception {

        ResultActions response = mockMvc.perform(get("/api/teachers/{id}/find/subjects", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenSubjectDTOObject_whenAddSubject_thenReturnSubjectDTOObject() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        SubjectDTO subjectDTO = createSubjectDTO();

        ResultActions response = mockMvc
                .perform(post("/api/teachers/{teacherId}/add/subject/{subjectId}", teacherDTO.getId(), subjectDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(subjectDTO.getId())))
                .andExpect(jsonPath("$.name", is(subjectDTO.getName())));
    }

    @Test
    void givenSubjectDTOObject_whenAddSubject_thenReturn400AlreadyHasSubject() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        SubjectDTO subjectDTO = createSubjectDTO();

        service.addSubject(teacherDTO.getId(), subjectDTO.getId());

        ResultActions response = mockMvc
                .perform(post("/api/teachers/{teacherId}/add/subject/{subjectId}", teacherDTO.getId(), subjectDTO.getId()));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenSubjectDTOObject_whenAddSubject_thenReturn404NotFoundSubject() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        ResultActions response = mockMvc
                .perform(post("/api/teachers/{teacherId}/add/subject/{subjectId}", teacherDTO.getId(), ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenSubjectDTOObject_whenAddSubject_thenReturn404NotFoundTeacher() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        ResultActions response = mockMvc
                .perform(post("/api/teachers/{teacherId}/add/subject/{subjectId}", ID_NOT_EXISTS, subjectDTO.getId()));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUpdatedSubjectDTOObject_whenUpdateSubject_thenReturnSubjectDTOObject() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        SubjectDTO oldSubject = createSubjectDTO();

        SubjectDTO newSubject = subjectService.save(new SubjectDTO("MATH"));

        service.addSubject(teacherDTO.getId(), oldSubject.getId());

        ResultActions response = mockMvc
                .perform(patch("/api/teachers/{teacherId}/update/{oldSubjectId}/subject/new/{newSubjectId}",
                        teacherDTO.getId(), oldSubject.getId(), newSubject.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(newSubject.getId())))
                .andExpect(jsonPath("$.name", is(newSubject.getName())));
    }

    @Test
    void givenUpdatedSubjectDTOObject_whenUpdateSubject_thenReturn404NotFoundTeacher() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        SubjectDTO oldSubject = createSubjectDTO();

        SubjectDTO newSubject = subjectService.save(new SubjectDTO("MATH"));

        service.addSubject(teacherDTO.getId(), oldSubject.getId());

        ResultActions response = mockMvc
                .perform(patch("/api/teachers/{teacherId}/update/{oldSubjectId}/subject/new/{newSubjectId}",
                        ID_NOT_EXISTS, oldSubject.getId(), newSubject.getId()));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUpdatedSubjectDTOObject_whenUpdateSubject_thenReturn404NotFoundOldSubject() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        SubjectDTO oldSubject = createSubjectDTO();

        SubjectDTO newSubject = subjectService.save(new SubjectDTO("MATH"));

        service.addSubject(teacherDTO.getId(), oldSubject.getId());

        ResultActions response = mockMvc
                .perform(patch("/api/teachers/{teacherId}/update/{oldSubjectId}/subject/new/{newSubjectId}",
                        teacherDTO.getId(), ID_NOT_EXISTS, newSubject.getId()));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUpdatedSubjectDTOObject_whenUpdateSubject_thenReturn404NotFoundNewSubject() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        SubjectDTO oldSubject = createSubjectDTO();

        service.addSubject(teacherDTO.getId(), oldSubject.getId());

        ResultActions response = mockMvc
                .perform(patch("/api/teachers/{teacherId}/update/{oldSubjectId}/subject/new/{newSubjectId}",
                        teacherDTO.getId(), oldSubject.getId(), ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenResponseEntity_whenDeleteSubject_thenReturn200() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        SubjectDTO subjectDTO = createSubjectDTO();

        service.addSubject(teacherDTO.getId(), subjectDTO.getId());

        ResultActions response = mockMvc
                .perform(delete("/api/teachers/{teacherId}/delete/{subjectId}/subject", teacherDTO.getId(), subjectDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenResponseEntity_whenDeleteSubject_thenReturn404NotFoundTeacher() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        SubjectDTO subjectDTO = createSubjectDTO();

        service.addSubject(teacherDTO.getId(), subjectDTO.getId());

        ResultActions response = mockMvc
                .perform(delete("/api/teachers/{teacherId}/delete/{subjectId}/subject", ID_NOT_EXISTS, subjectDTO.getId()));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenResponseEntity_whenDeleteSubject_thenReturn404NotFoundSubject() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        SubjectDTO subjectDTO = createSubjectDTO();

        service.addSubject(teacherDTO.getId(), subjectDTO.getId());

        ResultActions response = mockMvc
                .perform(delete("/api/teachers/{teacherId}/delete/{subjectId}/subject", teacherDTO.getId(), ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    private List<SubjectDTO> createSubjectsDTO(){
        SubjectDTO subjectDTOTest1 = new SubjectDTO();
        subjectDTOTest1.setName("SPORT");
        SubjectDTO subjectDTOTest2 = new SubjectDTO();
        subjectDTOTest2.setName("PHYSICAL");
        List<SubjectDTO> subjectsDTO = new ArrayList<>();
        subjectsDTO.add(subjectDTOTest1);
        subjectsDTO.add(subjectDTOTest2);
        return subjectsDTO.stream().map(subjectDTO -> subjectService.save(subjectDTO)).collect(Collectors.toList());
    }

    private SubjectDTO createSubjectDTO(){
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setName("CHEMISTRY");
        return subjectService.save(subjectDTO);
    }

    private List<TeacherDTO> createTeachersDTO(){
        List<TeacherDTO> teachersDTO = new ArrayList<>();
        teachersDTO.add(new TeacherDTO("Hurmek", "Polin"));
        teachersDTO.add(new TeacherDTO("Hurmek", "Elunin"));
        return teachersDTO.stream().map(teacherDTO -> service.save(teacherDTO)).collect(Collectors.toList());
    }

    private TeacherDTO createTeacherDTO(){
        return service.save(new TeacherDTO("Hurmek", "Polin"));
    }

    private TeacherCreateDTO createTeacherCreateDTO(){
        return new TeacherCreateDTO("Malik", "Jordan");
    }
}