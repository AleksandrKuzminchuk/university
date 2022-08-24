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
import ua.foxminded.task10.uml.dto.SubjectDTO;
import ua.foxminded.task10.uml.dto.TeacherDTO;
import ua.foxminded.task10.uml.dto.response.SubjectAddTeacherResponse;
import ua.foxminded.task10.uml.dto.response.SubjectUpdateTeacherResponse;
import ua.foxminded.task10.uml.service.SubjectService;
import ua.foxminded.task10.uml.service.TeacherService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.task10.uml.util.ConstantsTests.*;
import static ua.foxminded.task10.uml.util.MediaTypeTest.MEDIA_TYPE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(value = {"classpath:create-table-subjects.sql", "classpath:create-table-teachers_subjects.sql", "classpath:create-table-teachers.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SubjectControllerIntegrationTests {

    @Autowired
    private SubjectService service;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        service.deleteAll();
        teacherService.deleteAll();
    }

    @AfterEach
    void tearDown() {
        service.deleteAll();
        teacherService.deleteAll();
    }

    @Test
    void shouldReturnSubjectsDTOList_whenFindAll() throws Exception {

        List<SubjectDTO> subjectsDTO = createSubjectsDTO();

        ResultActions response = mockMvc.perform(get("/subjects"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("subjects", hasSize(2)))
                .andExpect(model().attribute("subjects", hasItem(
                        allOf(
                                hasProperty("id", is(subjectsDTO.get(0).getId())),
                                hasProperty("name", is(subjectsDTO.get(0).getName()))
                        )
                )))
                .andExpect(model().attribute("subjects", hasItem(
                        allOf(
                                hasProperty("id", is(subjectsDTO.get(1).getId())),
                                hasProperty("name", is(subjectsDTO.get(1).getName()))
                        )
                )))
                .andExpect(model().attribute("count", is(2)))
                .andExpect(view().name("subjects/subjects"));
    }

    @Test
    void shouldReturnSaveForm() throws Exception {

        ResultActions response = mockMvc.perform(get("/subjects/new")
                .contentType(MEDIA_TYPE)
                .sessionAttr("newSubject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("subjects/formForSaveSubject"));
    }

    @Test
    void shouldReturnSavedSubjectDTO_whenSave() throws Exception {

        SubjectDTO saveSubject = new SubjectDTO("GEOMETRY");

        ResultActions response = mockMvc.perform(post("/subjects/saved")
                .contentType(MEDIA_TYPE)
                .param("name", saveSubject.getName())
                .sessionAttr("newSubject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("subject", hasProperty("id", is(1))))
                .andExpect(model().attribute("subject", hasProperty("name", is(saveSubject.getName()))))
                .andExpect(view().name("subjects/formSavedSubject"));
    }

    @Test
    void shouldReturn400NotValid_whenSave() throws Exception {

        SubjectDTO saveSubject = new SubjectDTO("gEOMETRY");

        ResultActions response = mockMvc.perform(post("/subjects/saved")
                .contentType(MEDIA_TYPE)
                .param("name", saveSubject.getName())
                .sessionAttr("newSubject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUpdateForm() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        ResultActions response = mockMvc.perform(get("/subjects/{id}/update", subjectDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("subject", hasProperty("id", is(subjectDTO.getId()))))
                .andExpect(model().attribute("subject", hasProperty("name", is(subjectDTO.getName()))))
                .andExpect(view().name("subjects/formForUpdateSubject"));
    }

    @Test
    void shouldReturn404NotFound_whenUpdateForm() throws Exception {

        ResultActions response = mockMvc.perform(get("/subjects/{id}/update", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedSubjectDTO_whenUpdate() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        SubjectDTO updateSubject = new SubjectDTO("MATH");

        ResultActions response = mockMvc.perform(patch("/subjects/{id}/updated", subjectDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("name", updateSubject.getName())
                .sessionAttr("updateSubject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("subjectUpdated", hasProperty("id", is(subjectDTO.getId()))))
                .andExpect(model().attribute("subjectUpdated", hasProperty("name", is(updateSubject.getName()))))
                .andExpect(view().name("subjects/formUpdatedSubject"));
    }

    @Test
    void shouldReturn404NotFound_whenUpdate() throws Exception {

        SubjectDTO updateSubject = new SubjectDTO("MATH");

        ResultActions response = mockMvc.perform(patch("/subjects/{id}/updated", ID_NOT_EXISTS)
                .contentType(MEDIA_TYPE)
                .param("name", updateSubject.getName())
                .sessionAttr("updateSubject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400NotValid_whenUpdate() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        SubjectDTO updateSubject = new SubjectDTO("mATH");

        ResultActions response = mockMvc.perform(patch("/subjects/{id}/updated", subjectDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("name", updateSubject.getName())
                .sessionAttr("updateSubject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200_whenDeleteById() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        ResultActions response = mockMvc.perform(delete("/subjects/{id}/deleted", subjectDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("subjects/formDeletedSubject"));
    }

    @Test
    void shouldReturn404NotFound_whenDeleteById() throws Exception {

        ResultActions response = mockMvc.perform(delete("/subjects/{id}/deleted", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200_whenDeleteAll() throws Exception {

        createSubjectsDTO();

        ResultActions response = mockMvc.perform(delete("/subjects/delete/all"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("subjects/formDeleteAllSubjects"));
    }

    @Test
    void shouldReturnFindByNameForm() throws Exception {

        ResultActions response = mockMvc.perform(get("/subjects/find/by_name")
                .contentType(MEDIA_TYPE)
                .sessionAttr("subject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("subjects/formForFindSubjectByName"));
    }

    @Test
    void shouldReturnStudentDTOList_whenFindByName() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        ResultActions response = mockMvc.perform(get("/subjects/found/by_name")
                .contentType(MEDIA_TYPE)
                .param("name", subjectDTO.getName())
                .sessionAttr("findSubject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("subjects", hasProperty("id", is(subjectDTO.getId()))))
                .andExpect(model().attribute("subjects", hasProperty("name", is(subjectDTO.getName()))))
                .andExpect(view().name("subjects/subjects"));
    }

    @Test
    void shouldReturn400NotValid_whenFindByName() throws Exception {

        ResultActions response = mockMvc.perform(get("/subjects/found/by_name")
                .contentType(MEDIA_TYPE)
                .param("name", "gEOMETRY")
                .sessionAttr("findSubject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404NotFound_whenFindByName() throws Exception {

        ResultActions response = mockMvc.perform(get("/subjects/found/by_name")
                .contentType(MEDIA_TYPE)
                .param("name", "MATH")
                .sessionAttr("findSubject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAddTeacherForm() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        SubjectAddTeacherResponse addTeacherResponse = service.addTeacherForm(subjectDTO.getId());

        ResultActions response = mockMvc.perform(get("/subjects/{id}/add/teacher", subjectDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("addTeacher", addTeacherResponse))
                .andExpect(model().attribute("teacher", new TeacherDTO()))
                .andExpect(view().name("subjects/formForAddSubjectToTeacher"));
    }

    @Test
    void shouldReturn404NotFound_whenAddTeacherForm() throws Exception {

        ResultActions response = mockMvc.perform(get("/subjects/{id}/add/teacher", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200_whenAddTeacher() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        TeacherDTO teacherDTO = createTeacherDTO();

        ResultActions response = mockMvc.perform(post("/subjects/{id}/added/teacher", subjectDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("id", String.valueOf(teacherDTO.getId()))
                .sessionAttr("addTeacher", TeacherDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(view().name("subjects/formAddedSubjectToTeacher"));
    }

    @Test
    void shouldReturn404NotFoundSubject_whenAddTeacher() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        ResultActions response = mockMvc.perform(post("/subjects/{id}/added/teacher", ID_NOT_EXISTS)
                .contentType(MEDIA_TYPE)
                .param("id", String.valueOf(teacherDTO.getId()))
                .sessionAttr("addTeacher", TeacherDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundTeacher_whenAddTeacher() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        ResultActions response = mockMvc.perform(post("/subjects/{id}/added/teacher", subjectDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("id", String.valueOf(ID_NOT_EXISTS))
                .sessionAttr("addTeacher", TeacherDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnTeachersDTOList_whenFindTeachers() throws Exception {

        List<TeacherDTO> teachersDTO = createTeachersDTO();

        SubjectDTO subjectDTO = createSubjectDTO();

        teachersDTO.forEach(teacherDTO -> service.addTeacher(subjectDTO.getId(), teacherDTO.getId()));

        ResultActions response = mockMvc.perform(get("/subjects/{id}/show/teachers", subjectDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("teachers", hasSize(2)))
                .andExpect(model().attribute("teachers", hasItem(
                        allOf(
                                hasProperty("id", is(teachersDTO.get(0).getId())),
                                hasProperty("firstName", is(teachersDTO.get(0).getFirstName())),
                                hasProperty("lastName", is(teachersDTO.get(0).getLastName()))
                        )
                )))
                .andExpect(model().attribute("teachers", hasItem(
                        allOf(
                                hasProperty("id", is(teachersDTO.get(1).getId())),
                                hasProperty("firstName", is(teachersDTO.get(1).getFirstName())),
                                hasProperty("lastName", is(teachersDTO.get(1).getLastName()))
                        )
                )))
                .andExpect(model().attribute("subject", hasProperty("id", is(subjectDTO.getId()))))
                .andExpect(model().attribute("subject", hasProperty("name", is(subjectDTO.getName()))))
                .andExpect(model().attribute("count", is(2)))
                .andExpect(view().name("subjects/formFindTeachersToSubject"));
    }

    @Test
    void shouldReturn404NotFound_whenFindTeachers() throws Exception {

        ResultActions response = mockMvc.perform(get("/subjects/{id}/show/teachers", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUpdateTeacherForm() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        TeacherDTO teacherDTO = createTeacherDTO();

        SubjectUpdateTeacherResponse updateTeacherResponse = service.updateTeacherForm(subjectDTO.getId(), teacherDTO.getId());

        ResultActions response = mockMvc.perform(get("/subjects/{subjectId}/update/{teacherId}/teacher",
                subjectDTO.getId(), teacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("newTeacher", new TeacherDTO()))
                .andExpect(model().attribute("updateTeacher", updateTeacherResponse))
                .andExpect(view().name("subjects/formUpdateTheSubjectTeacher"));
    }

    @Test
    void shouldReturn404NotFoundSubject_whenUpdateTeacherForm() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        ResultActions response = mockMvc.perform(get("/subjects/{subjectId}/update/{teacherId}/teacher",
                ID_NOT_EXISTS, teacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundTeacher_whenUpdateTeacherForm() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        ResultActions response = mockMvc.perform(get("/subjects/{subjectId}/update/{teacherId}/teacher",
                subjectDTO.getId(), ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedTeacherDTO_whenUpdateTeacher() throws Exception {

        List<TeacherDTO> teachersDTO = createTeachersDTO();

        SubjectDTO subjectDTO = createSubjectDTO();

        service.addTeacher(subjectDTO.getId(), teachersDTO.get(0).getId());

        ResultActions response = mockMvc.perform(patch("/subjects/{subjectId}/updated/{oldTeacherId}/teacher",
                subjectDTO.getId(), teachersDTO.get(0).getId())
                .contentType(MEDIA_TYPE)
                .param("id", String.valueOf(teachersDTO.get(1).getId()))
                .sessionAttr("newTeacher", TeacherDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("subject", is(subjectDTO.getId())))
                .andExpect(view().name("subjects/formUpdatedTheSubjectTeacher"));
    }

    @Test
    void shouldReturn404NotFoundSubject_whenUpdateTeacher() throws Exception {

        List<TeacherDTO> teachersDTO = createTeachersDTO();

        SubjectDTO subjectDTO = createSubjectDTO();

        service.addTeacher(subjectDTO.getId(), teachersDTO.get(0).getId());

        ResultActions response = mockMvc.perform(patch("/subjects/{subjectId}/updated/{oldTeacherId}/teacher",
                ID_NOT_EXISTS, teachersDTO.get(0).getId())
                .contentType(MEDIA_TYPE)
                .param("id", String.valueOf(teachersDTO.get(1).getId()))
                .sessionAttr("newTeacher", TeacherDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundOldTeacher_whenUpdateTeacher() throws Exception {

        List<TeacherDTO> teachersDTO = createTeachersDTO();

        SubjectDTO subjectDTO = createSubjectDTO();

        service.addTeacher(subjectDTO.getId(), teachersDTO.get(0).getId());

        ResultActions response = mockMvc.perform(patch("/subjects/{subjectId}/updated/{oldTeacherId}/teacher",
                subjectDTO.getId(), ID_NOT_EXISTS)
                .contentType(MEDIA_TYPE)
                .param("id", String.valueOf(teachersDTO.get(1).getId()))
                .sessionAttr("newTeacher", TeacherDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundNewTeacher_whenUpdateTeacher() throws Exception {

        List<TeacherDTO> teachersDTO = createTeachersDTO();

        SubjectDTO subjectDTO = createSubjectDTO();

        service.addTeacher(subjectDTO.getId(), teachersDTO.get(0).getId());

        ResultActions response = mockMvc.perform(patch("/subjects/{subjectId}/updated/{oldTeacherId}/teacher",
                subjectDTO.getId(), teachersDTO.get(0).getId())
                .contentType(MEDIA_TYPE)
                .param("id", String.valueOf(ID_NOT_EXISTS))
                .sessionAttr("newTeacher", TeacherDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400BadRequestedSubjectHasAlreadyTeacher_whenUpdateTeacher() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        SubjectDTO subjectDTO = createSubjectDTO();

        service.addTeacher(subjectDTO.getId(), teacherDTO.getId());

        ResultActions response = mockMvc.perform(patch("/subjects/{subjectId}/updated/{oldTeacherId}/teacher",
                subjectDTO.getId(), teacherDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("id", String.valueOf(teacherDTO.getId()))
                .sessionAttr("newTeacher", TeacherDTO.class));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200_whenDeleteTeacher() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        TeacherDTO teacherDTO = createTeacherDTO();

        service.addTeacher(subjectDTO.getId(), teacherDTO.getId());

        ResultActions response = mockMvc.perform(delete("/subjects/{subjectId}/deleted/{teacherId}/teacher",
                subjectDTO.getId(), teacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("subject", subjectDTO.getId()))
                .andExpect(view().name("subjects/formDeleteTheSubjectTeacher"));
    }

    @Test
    void shouldReturn404NotFoundSubject_whenDeleteTeacher() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        ResultActions response = mockMvc.perform(delete("/subjects/{subjectId}/deleted/{teacherId}/teacher",
                ID_NOT_EXISTS, teacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundTeacher_whenDeleteTeacher() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        ResultActions response = mockMvc.perform(delete("/subjects/{subjectId}/deleted/{teacherId}/teacher",
                subjectDTO.getId(), ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundSubjectHasNotTeacher_whenDeleteTeacher() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        TeacherDTO teacherDTO = createTeacherDTO();

        ResultActions response = mockMvc.perform(delete("/subjects/{subjectId}/deleted/{teacherId}/teacher",
                subjectDTO.getId(), teacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    private List<SubjectDTO> createSubjectsDTO() {
        SubjectDTO subjectDTOTest1 = new SubjectDTO();
        subjectDTOTest1.setName("GEOMETRY");
        SubjectDTO subjectDTOTest2 = new SubjectDTO();
        subjectDTOTest2.setName("MATH");
        List<SubjectDTO> subjectsDTO = new ArrayList<>();
        subjectsDTO.add(subjectDTOTest1);
        subjectsDTO.add(subjectDTOTest2);
        return subjectsDTO.stream().map(subjectDTO -> service.save(subjectDTO)).collect(Collectors.toList());
    }

    private SubjectDTO createSubjectDTO() {
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setName("GEOMETRY");
        return service.save(subjectDTO);
    }

    private List<TeacherDTO> createTeachersDTO() {
        List<TeacherDTO> teachersDTO = new ArrayList<>();
        teachersDTO.add(new TeacherDTO("Furler", "Jurkeb"));
        teachersDTO.add(new TeacherDTO("Karl", "Markovich"));
        return teachersDTO.stream().map(teacherDTO -> teacherService.save(teacherDTO)).collect(Collectors.toList());
    }

    private TeacherDTO createTeacherDTO() {
        return teacherService.save(new TeacherDTO("Furler", "Jurkeb"));
    }
}