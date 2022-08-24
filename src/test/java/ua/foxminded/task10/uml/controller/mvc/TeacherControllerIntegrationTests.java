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
import ua.foxminded.task10.uml.dto.response.TeacherAddSubjectResponse;
import ua.foxminded.task10.uml.dto.response.TeacherUpdateSubjectResponse;
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
@Sql(value = {"classpath:create-table-teachers.sql", "classpath:create-table-teachers_subjects.sql", "classpath:create-table-subjects.sql"},
executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TeacherControllerIntegrationTests {

    @Autowired
    private TeacherService service;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        service.deleteAll();
        subjectService.deleteAll();
    }

    @AfterEach
    void tearDown() {
        service.deleteAll();
        subjectService.deleteAll();
    }

    @Test
    void shouldReturnTeachersDTOList_whenFindAll() throws Exception {

        List<TeacherDTO> teachersDTO = createTeachersDTO();

        ResultActions response = mockMvc.perform(get("/teachers"));

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
                .andExpect(view().name("teachers/teachers"));
    }

    @Test
    void shouldReturnSaveForm() throws Exception {

        ResultActions response = mockMvc.perform(get("/teachers/new")
                .contentType(MEDIA_TYPE)
                .sessionAttr("newTeacher", TeacherDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(view().name("teachers/formSaveTeacher"));
    }

    @Test
    void shouldReturnSavedTeacherDTO_whenSave() throws Exception {

        TeacherDTO teacherDTO = new TeacherDTO("Mark", "Jurai");

        ResultActions response = mockMvc.perform(post("/teachers/saved")
                .contentType(MEDIA_TYPE)
                .param("firstName", teacherDTO.getFirstName())
                .param("lastName", teacherDTO.getLastName())
                .sessionAttr("newTeacher", TeacherDTO.class));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("teacher", hasProperty("id", is(1))))
                .andExpect(model().attribute("teacher", hasProperty("firstName", is(teacherDTO.getFirstName()))))
                .andExpect(model().attribute("teacher", hasProperty("lastName", is(teacherDTO.getLastName()))))
                .andExpect(view().name("teachers/formSavedTeacher"));
    }

    @Test
    void shouldReturn400NotValid_whenSave() throws Exception {

        TeacherDTO teacherDTO = new TeacherDTO("mark", "jurai");

        ResultActions response = mockMvc.perform(post("/teachers/saved")
                .contentType(MEDIA_TYPE)
                .param("firstName", teacherDTO.getFirstName())
                .param("lastName", teacherDTO.getLastName())
                .sessionAttr("newTeacher", TeacherDTO.class));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200_whenDeleteById() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        ResultActions response = mockMvc.perform(delete("/teachers/{id}/deleted", teacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/formDeletedTeacher"));
    }

    @Test
    void shouldReturn404NotFound_whenDeleteById() throws Exception {

        ResultActions response = mockMvc.perform(delete("/teachers/{id}/deleted", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUpdateForm() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        ResultActions response = mockMvc.perform(get("/teachers/{id}/update", teacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("teacher", hasProperty("id", is(teacherDTO.getId()))))
                .andExpect(model().attribute("teacher", hasProperty("firstName", is(teacherDTO.getFirstName()))))
                .andExpect(model().attribute("teacher", hasProperty("lastName", is(teacherDTO.getLastName()))))
                .andExpect(view().name("teachers/formUpdateTeacher"));
    }

    @Test
    void shouldReturn404NotFound_whenUpdateForm() throws Exception {

        ResultActions response = mockMvc.perform(get("/teachers/{id}/update", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedTeacherDTO_whenUpdate() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        TeacherDTO updateTeacher = new TeacherDTO("Gerard", "Louren");

        ResultActions response = mockMvc.perform(patch("/teachers/{id}/updated", teacherDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("firstName", updateTeacher.getFirstName())
                .param("lastName", updateTeacher.getLastName())
                .sessionAttr("updateTeacher", TeacherDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("updatedTeacher", hasProperty("id", is(teacherDTO.getId()))))
                .andExpect(model().attribute("updatedTeacher", hasProperty("firstName", is(updateTeacher.getFirstName()))))
                .andExpect(model().attribute("updatedTeacher", hasProperty("lastName", is(updateTeacher.getLastName()))))
                .andExpect(view().name("teachers/formUpdatedTeacher"));
    }

    @Test
    void shouldReturn404NotFound_whenUpdate() throws Exception {

        TeacherDTO updateTeacher = new TeacherDTO("Gerard", "Louren");

        ResultActions response = mockMvc.perform(patch("/teachers/{id}/updated", ID_NOT_EXISTS)
                .contentType(MEDIA_TYPE)
                .param("firstName", updateTeacher.getFirstName())
                .param("lastName", updateTeacher.getLastName())
                .sessionAttr("updateTeacher", TeacherDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400NotValid_whenUpdate() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        TeacherDTO updateTeacher = new TeacherDTO("gerard", "louren");

        ResultActions response = mockMvc.perform(patch("/teachers/{id}/updated", teacherDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("firstName", updateTeacher.getFirstName())
                .param("lastName", updateTeacher.getLastName())
                .sessionAttr("updateTeacher", TeacherDTO.class));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUpdateSubjectForm() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        TeacherDTO teacherDTO = createTeacherDTO();

        TeacherUpdateSubjectResponse teacherUpdateSubjectResponse = service.updateSubjectForm(teacherDTO.getId(), subjectDTO.getId());

        ResultActions response = mockMvc.perform(get("/teachers/{teacherId}/update/{subjectId}/subject"
        , teacherDTO.getId(), subjectDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("newSubject", new SubjectDTO()))
                .andExpect(model().attribute("updateSubject", teacherUpdateSubjectResponse))
                .andExpect(view().name("teachers/formUpdateTheTeacherSubject"));
    }

    @Test
    void shouldReturnUpdatedSubjectDTO_whenUpdateSubjectForm() throws Exception {

        List<SubjectDTO> subjectsDTO = createSubjectsDTO();

        TeacherDTO teacherDTO = createTeacherDTO();
        service.addSubject(teacherDTO.getId(), subjectsDTO.get(0).getId());

        ResultActions response = mockMvc.perform(patch("/teachers/{teacherId}/updated/{oldSubjectId}/subject",
                teacherDTO.getId(), subjectsDTO.get(0).getId())
                .contentType(MEDIA_TYPE)
                .param("id", String.valueOf(subjectsDTO.get(1).getId()))
                .sessionAttr("newSubject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("teacher", is(teacherDTO.getId())))
                .andExpect(view().name("teachers/formUpdatedTheTeacherSubject"));
    }

    @Test
    void shouldReturn400BadRequestedTeacherHasAlreadySubject_whenUpdateSubjectForm() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        TeacherDTO teacherDTO = createTeacherDTO();
        service.addSubject(teacherDTO.getId(), subjectDTO.getId());

        ResultActions response = mockMvc.perform(patch("/teachers/{teacherId}/updated/{oldSubjectId}/subject",
                teacherDTO.getId(), subjectDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("id", String.valueOf(subjectDTO.getId()))
                .sessionAttr("newSubject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404NotFoundTeacher_whenUpdateSubjectForm() throws Exception {

        List<SubjectDTO> subjectsDTO = createSubjectsDTO();

        TeacherDTO teacherDTO = createTeacherDTO();
        service.addSubject(teacherDTO.getId(), subjectsDTO.get(0).getId());

        ResultActions response = mockMvc.perform(patch("/teachers/{teacherId}/updated/{oldSubjectId}/subject",
                ID_NOT_EXISTS, subjectsDTO.get(0).getId())
                .contentType(MEDIA_TYPE)
                .param("id", String.valueOf(subjectsDTO.get(1).getId()))
                .sessionAttr("newSubject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundNewSubject_whenUpdateSubjectForm() throws Exception {

        List<SubjectDTO> subjectsDTO = createSubjectsDTO();

        TeacherDTO teacherDTO = createTeacherDTO();
        service.addSubject(teacherDTO.getId(), subjectsDTO.get(0).getId());

        ResultActions response = mockMvc.perform(patch("/teachers/{teacherId}/updated/{oldSubjectId}/subject",
                teacherDTO.getId(), subjectsDTO.get(0).getId())
                .contentType(MEDIA_TYPE)
                .param("id", String.valueOf(ID_NOT_EXISTS))
                .sessionAttr("newSubject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundOldSubject_whenUpdateSubjectForm() throws Exception {

        List<SubjectDTO> subjectsDTO = createSubjectsDTO();

        TeacherDTO teacherDTO = createTeacherDTO();
        service.addSubject(teacherDTO.getId(), subjectsDTO.get(0).getId());

        ResultActions response = mockMvc.perform(patch("/teachers/{teacherId}/updated/{oldSubjectId}/subject",
                teacherDTO.getId(), ID_NOT_EXISTS)
                .contentType(MEDIA_TYPE)
                .param("id", String.valueOf(subjectsDTO.get(1).getId()))
                .sessionAttr("newSubject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200_whenDeleteAll() throws Exception {

        createTeachersDTO();

        ResultActions response = mockMvc.perform(delete("/teachers/deleted/all"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/deleteAllTeachers"));
    }

    @Test
    void shouldReturnFindByNameOrSurnameForm() throws Exception {

        ResultActions response = mockMvc.perform(get("/teachers/find/by_name_surname")
                .contentType(MEDIA_TYPE)
                .sessionAttr("newTeacher", TeacherDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/formFindTeacherByNameSurname"));
    }

    @Test
    void shouldReturnTeacherDTOList_whenFindByNameOrSurname() throws Exception {

        createTeachersDTO();

        ResultActions response = mockMvc.perform(get("/teachers/found/by_name_surname")
                .contentType(MEDIA_TYPE)
                .param("firstName", "Hurmek")
                .param("lastName", "Gurin")
                .sessionAttr("newTeacher", TeacherDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("teachers", hasSize(2)))
                .andExpect(model().attribute("teachers", hasItem(
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("firstName", is("Hurmek")),
                                hasProperty("lastName", is("Polin"))
                        )
                )))
                .andExpect(model().attribute("teachers", hasItem(
                        allOf(
                                hasProperty("id", is(2)),
                                hasProperty("firstName", is("Hurmek")),
                                hasProperty("lastName", is("Elunin"))
                        )
                )))
                .andExpect(model().attribute("count", is(2)))
                .andExpect(view().name("teachers/teachers"));
    }

    @Test
    void shouldReturnAddSubjectForm() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        TeacherAddSubjectResponse teacherAddSubjectResponse = service.addSubjectFrom(teacherDTO.getId());

        ResultActions response = mockMvc.perform(get("/teachers/{id}/add/subject", teacherDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("addSubject", teacherAddSubjectResponse))
                .andExpect(model().attribute("subject", new SubjectDTO()))
                .andExpect(view().name("teachers/formForAddTeacherToSubject"));
    }

    @Test
    void shouldReturn404NotFound_whenAddSubjectForm() throws Exception {

        ResultActions response = mockMvc.perform(get("/teachers/{id}/add/subject", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200_whenAddSubject() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        TeacherDTO teacherDTO = createTeacherDTO();

        ResultActions response = mockMvc.perform(post("/teachers/{id}/added/subject", teacherDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("addSubject", String.valueOf(subjectDTO.getId()))
                .sessionAttr("addSubject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(view().name("teachers/formAddedTeacherToSubject"));
    }

    @Test
    void shouldReturn404NotFoundTeacher_whenAddSubject() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        ResultActions response = mockMvc.perform(post("/teachers/{id}/added/subject", ID_NOT_EXISTS)
                .contentType(MEDIA_TYPE)
                .param("addSubject", String.valueOf(subjectDTO.getId()))
                .sessionAttr("addSubject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundSubject_whenAddSubject() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        ResultActions response = mockMvc.perform(post("/teachers/{id}/added/subject", teacherDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("addSubject", String.valueOf(ID_NOT_EXISTS))
                .sessionAttr("addSubject", SubjectDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnSubjectDTOList_whenFindSubjects() throws Exception {

        List<SubjectDTO> subjectsDTO = createSubjectsDTO();

        TeacherDTO teacherDTO = createTeacherDTO();

        subjectsDTO.stream().forEach(subjectDTO -> service.addSubject(teacherDTO.getId(), subjectDTO.getId()));

        ResultActions response = mockMvc.perform(get("/teachers/{id}/show/subjects", teacherDTO.getId()));

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
                .andExpect(model().attribute("teacher", hasProperty("id", is(teacherDTO.getId()))))
                .andExpect(model().attribute("teacher", hasProperty("firstName", is(teacherDTO.getFirstName()))))
                .andExpect(model().attribute("teacher", hasProperty("lastName", is(teacherDTO.getLastName()))))
                .andExpect(view().name("teachers/formShowSubjectsByTeacherId"));
    }

    @Test
    void shouldReturn404NotFound_whenFindSubjects() throws Exception {

        ResultActions response = mockMvc.perform(get("/teachers/{id}/show/subjects", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200_whenDeleteSubject() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        TeacherDTO teacherDTO = createTeacherDTO();

        service.addSubject(teacherDTO.getId(), subjectDTO.getId());

        ResultActions response = mockMvc.perform(delete("/teachers/{teacherId}/deleted/{subjectId}/subject",
                teacherDTO.getId(), subjectDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("teacher", is(teacherDTO.getId())))
                .andExpect(view().name("teachers/formDeleteTheTeacherSubject"));
    }

    @Test
    void shouldReturn404NotFoundTeacher_whenDeleteSubject() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        ResultActions response = mockMvc.perform(delete("/teachers/{teacherId}/deleted/{subjectId}/subject",
                ID_NOT_EXISTS, subjectDTO.getId()));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundSubject_whenDeleteSubject() throws Exception {

        TeacherDTO teacherDTO = createTeacherDTO();

        ResultActions response = mockMvc.perform(delete("/teachers/{teacherId}/deleted/{subjectId}/subject",
                teacherDTO.getId(), ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404NotFoundTeacherHasNotSubject_whenDeleteSubject() throws Exception {

        SubjectDTO subjectDTO = createSubjectDTO();

        TeacherDTO teacherDTO = createTeacherDTO();

        ResultActions response = mockMvc.perform(delete("/teachers/{teacherId}/deleted/{subjectId}/subject",
                teacherDTO.getId(), subjectDTO.getId()));

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
}