package ua.foxminded.task10.uml.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ua.foxminded.task10.uml.dto.SubjectCreateDTO;
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;
import ua.foxminded.task10.uml.service.SubjectService;
import ua.foxminded.task10.uml.service.TeacherService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SubjectRestControllerIntegrationTest {

    @Autowired
    private SubjectService service;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        teacherService.deleteAll();
        service.deleteAll();
    }

    @Test
    void givenSubjectsDTOList_whenFindAll_thenReturnSubjectsDTOList() throws Exception {

        createSubjectsDTO();

        ResultActions response = mockMvc.perform(get("/api/subjects"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.subjects.size()", is(2)));
    }

    @Test
    void givenSubjectDTOObject_whenCreate_thenReturnSubjectDTOObject() throws Exception {

        SubjectCreateDTO subjectCreateDTO = new SubjectCreateDTO();
        subjectCreateDTO.setName("GEOMETRY");

        ResultActions response = mockMvc.perform(post("/api/subjects/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subjectCreateDTO)));

        response.andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(createSubjectDTO().getName())));
    }

    @Test
    void givenSubjectDTOObject_whenCreate_thenReturn400NotValid() throws Exception {

        SubjectCreateDTO subjectCreateDTO = new SubjectCreateDTO();
        subjectCreateDTO.setName("gEOMETRY");

        ResultActions response = mockMvc.perform(post("/api/subjects/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subjectCreateDTO)));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenUpdatedSubjectDTOObject_whenUpdate_thenReturnUpdatedSubjectDTOObject() throws Exception {

        SubjectDTO savedSubject = createSubjectDTO();

        SubjectDTO updateSubject = new SubjectDTO();
        updateSubject.setName("MATH");

        ResultActions response = mockMvc.perform(patch("/api/subjects/update/{id}", savedSubject.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateSubject)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(savedSubject.getId())))
                .andExpect(jsonPath("$.name", is(updateSubject.getName())));
    }

    @Test
    void givenUpdatedSubjectDTOObject_whenUpdate_thenReturnUpdated400NotValid() throws Exception {

        SubjectDTO savedSubject = createSubjectDTO();

        SubjectDTO updateSubject = new SubjectDTO();
        updateSubject.setName("mATH");

        ResultActions response = mockMvc.perform(patch("/api/subjects/update/{id}", savedSubject.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateSubject)));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenUpdatedSubjectDTOObject_whenUpdate_thenReturnUpdated404NotFound() throws Exception {

        SubjectDTO updateSubject = new SubjectDTO();
        updateSubject.setName("MATH");

        ResultActions response = mockMvc.perform(patch("/api/subjects/update/50")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateSubject)));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenResponseEntity_whenDeleteById_thenReturn200() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        ResultActions response = mockMvc.perform(delete("/api/subjects/{id}/delete", subjectDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenResponseEntity_whenDeleteById_thenReturn404NotFound() throws Exception {

        ResultActions response = mockMvc.perform(delete("/api/subjects/45/delete"));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenResponseEntity_whenDeleteAll_thenReturn200() throws Exception {

        createSubjectsDTO();

        ResultActions response = mockMvc.perform(delete("/api/subjects/delete/all"));

        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenSubjectDTOObject_whenFindByName_thenReturnSubjectDTOObject() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        HttpHeaders headers = new HttpHeaders();
        headers.set("name", "GEOMETRY");

        ResultActions response = mockMvc.perform(get("/api/subjects/find/by_name")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(subjectDTO.getId())))
                .andExpect(jsonPath("$.name", is(subjectDTO.getName())));
    }

    @Test
    void givenSubjectDTOObject_whenFindByName_thenReturn404NotFound() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.set("name", "GEOMETRY");

        ResultActions response = mockMvc.perform(get("/api/subjects/find/by_name")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenTeacherDTOObject_whenAddTeacher_thenReturnTeacherDTOObject() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        SubjectDTO subjectDTO = createSubjectDTO();

        ResultActions response = mockMvc
                .perform(post("/api/subjects/{subjectId}/add/{teacherId}/teacher", subjectDTO.getId(), teacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(teacherDTO.getId())))
                .andExpect(jsonPath("$.firstName", is(teacherDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(teacherDTO.getLastName())));
    }

    @Test
    void givenTeacherDTOObject_whenAddTeacher_thenReturn400AlreadyHasTeacher() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        SubjectDTO subjectDTO = createSubjectDTO();

        service.addTeacher(subjectDTO.getId(), teacherDTO.getId());

        ResultActions response = mockMvc
                .perform(post("/api/subjects/{subjectId}/add/{teacherId}/teacher", subjectDTO.getId(), teacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenTeacherDTOObject_whenAddTeacher_thenReturn404NotFoundSubject() throws Exception {

        Integer idSubject = 14;

        TeacherDTO teacherDTO = createTeacherDTO();

        ResultActions response = mockMvc
                .perform(post("/api/subjects/{subjectId}/add/{teacherId}/teacher", idSubject, teacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenTeacherDTOObject_whenAddTeacher_thenReturn404NotFoundTeacher() throws Exception {

        Integer idTeacher = 14;

        SubjectDTO subjectDTO = createSubjectDTO();

        ResultActions response = mockMvc
                .perform(post("/api/subjects/{subjectId}/add/{teacherId}/teacher", subjectDTO.getId(), idTeacher));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    void givenTeachersDTOList_FindTeachers_thenReturnTeachersDTOList() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        List<TeacherDTO> teachersDTO = createTeachersDTO();

        teachersDTO.forEach(teacherDTO -> service.addTeacher(subjectDTO.getId(), teacherDTO.getId()));

        ResultActions response = mockMvc.perform(get("/api/subjects/{id}/find/teachers", subjectDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.teachers.size()", is(2)));
    }

    @Test
    void givenTeachersDTOList_FindTeachers_thenReturn404NotFound() throws Exception {

        ResultActions response = mockMvc.perform(get("/api/subjects/45/find/teachers"));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUpdateTeacherDTOObject_whenUpdateTeacher_thenReturnTeacherDTOObject() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        TeacherDTO oldTeacherDTO = createTeacherDTO();

        TeacherDTO newTeacherDTO = teacherService.save(new TeacherDTO("Hurmek", "Jarkin"));

        service.addTeacher(subjectDTO.getId(), oldTeacherDTO.getId());

        ResultActions response = mockMvc
                .perform(patch("/api/subjects/{subjectId}/update/{oldTeacherId}/teacher/new/{newTeacherId}",
                        subjectDTO.getId(), oldTeacherDTO.getId(), newTeacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(newTeacherDTO.getId())))
                .andExpect(jsonPath("$.firstName", is(newTeacherDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(newTeacherDTO.getLastName())));
    }

    @Test
    void givenUpdateTeacherDTOObject_whenUpdateTeacher_thenReturn404NotFoundSubject() throws Exception {

        Integer idSubject = 14;

        SubjectDTO subjectDTO = createSubjectDTO();

        TeacherDTO oldTeacherDTO = createTeacherDTO();

        TeacherDTO newTeacherDTO = teacherService.save(new TeacherDTO("Hurmek", "Jarkin"));

        service.addTeacher(subjectDTO.getId(), oldTeacherDTO.getId());

        ResultActions response = mockMvc
                .perform(patch("/api/subjects/{subjectId}/update/{oldTeacherId}/teacher/new/{newTeacherId}",
                         idSubject, oldTeacherDTO.getId(), newTeacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUpdateTeacherDTOObject_whenUpdateTeacher_thenReturn404NotFoundOldTeacher() throws Exception {

        Integer idOldTeacher = 14;

        SubjectDTO subjectDTO = createSubjectDTO();

        TeacherDTO oldTeacherDTO = createTeacherDTO();

        TeacherDTO newTeacherDTO = teacherService.save(new TeacherDTO("Hurmek", "Jarkin"));

        service.addTeacher(subjectDTO.getId(), oldTeacherDTO.getId());

        ResultActions response = mockMvc
                .perform(patch("/api/subjects/{subjectId}/update/{oldTeacherId}/teacher/new/{newTeacherId}",
                        subjectDTO.getId(), idOldTeacher, newTeacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUpdateTeacherDTOObject_whenUpdateTeacher_thenReturn404NotFoundNewTeacher() throws Exception {

        Integer idNewTeacher = 58;

        SubjectDTO subjectDTO = createSubjectDTO();

        TeacherDTO oldTeacherDTO = createTeacherDTO();

        service.addTeacher(subjectDTO.getId(), oldTeacherDTO.getId());

        ResultActions response = mockMvc
                .perform(patch("/api/subjects/{subjectId}/update/{oldTeacherId}/teacher/new/{newTeacherId}",
                        subjectDTO.getId(), oldTeacherDTO.getId(), idNewTeacher));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenResponseEntity_whenDeleteTeacher_thenReturn200() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        TeacherDTO teacherDTO = createTeacherDTO();

        service.addTeacher(subjectDTO.getId(), teacherDTO.getId());

        ResultActions response = mockMvc
                .perform(delete("/api/subjects/{subjectId}/delete/{teacherId}/teacher", subjectDTO.getId(), teacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenResponseEntity_whenDeleteTeacher_thenReturn404NotFoundSubject() throws Exception {

        Integer idSubject = 14;

        SubjectDTO subjectDTO = createSubjectDTO();

        TeacherDTO teacherDTO = createTeacherDTO();

        service.addTeacher(subjectDTO.getId(), teacherDTO.getId());

        ResultActions response = mockMvc
                .perform(delete("/api/subjects/{subjectId}/delete/{teacherId}/teacher", idSubject, teacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenResponseEntity_whenDeleteTeacher_thenReturn404NotFoundTeacher() throws Exception {

        Integer idTeacher = 14;

        SubjectDTO subjectDTO = createSubjectDTO();

        TeacherDTO teacherDTO = createTeacherDTO();

        service.addTeacher(subjectDTO.getId(), teacherDTO.getId());

        ResultActions response = mockMvc
                .perform(delete("/api/subjects/{subjectId}/delete/{teacherId}/teacher", subjectDTO.getId(), idTeacher));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    private List<SubjectDTO> createSubjectsDTO(){
        SubjectDTO subjectDTOTest1 = new SubjectDTO();
        subjectDTOTest1.setName("GEOMETRY");
        SubjectDTO subjectDTOTest2 = new SubjectDTO();
        subjectDTOTest2.setName("MATH");
        List<SubjectDTO> subjectsDTO = new ArrayList<>();
        subjectsDTO.add(subjectDTOTest1);
        subjectsDTO.add(subjectDTOTest2);
        return subjectsDTO.stream().map(subjectDTO -> service.save(subjectDTO)).collect(Collectors.toList());
    }

    private SubjectDTO createSubjectDTO(){
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setName("GEOMETRY");
        return service.save(subjectDTO);
    }

    private List<TeacherDTO> createTeachersDTO(){
        List<TeacherDTO> teachersDTO = new ArrayList<>();
        teachersDTO.add(new TeacherDTO("Furler", "Jurkeb"));
        teachersDTO.add(new TeacherDTO("Karl", "Markovich"));
        return teachersDTO.stream().map(teacherDTO -> teacherService.save(teacherDTO)).collect(Collectors.toList());
    }

    private TeacherDTO createTeacherDTO(){
        return teacherService.save(new TeacherDTO("Furler", "Jurkeb"));
    }
}