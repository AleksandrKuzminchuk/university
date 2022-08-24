package ua.foxminded.task10.uml.controller.mvc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ua.foxminded.task10.uml.dto.ClassroomDTO;
import ua.foxminded.task10.uml.service.ClassroomService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static ua.foxminded.task10.uml.util.ConstantsTests.ID_NOT_EXISTS;
import static ua.foxminded.task10.uml.util.MediaTypeTest.MEDIA_TYPE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(value = "classpath:create-table-classrooms.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ClassroomControllerIntegrationTests {

    @Autowired
    private ClassroomService service;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        service.deleteAll();
    }

    @AfterEach
    void tearDown() {
        service.deleteAll();
    }

    @Test
    void shouldReturnClassroomsDTOList() throws Exception {

        List<ClassroomDTO> classroomsDTO = createClassroomsDTO();

        ResultActions response = mockMvc.perform(get("/classrooms"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf(MEDIA_TYPE)))
                .andExpect(model().attribute("classrooms", classroomsDTO.stream().
                        sorted((x, y) -> x.getNumber().compareTo(y.getNumber())).collect(Collectors.toList())))
                .andExpect(model().attribute("classrooms", hasSize(2)))
                .andExpect(model().attribute("classrooms", hasItem(
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("number", is(45))
                        )
                )))
                .andExpect(model().attribute("classrooms", hasItem(
                        allOf(
                                hasProperty("id", is(2)),
                                hasProperty("number", is(5))
                        )
                )))
                .andExpect(model().attribute("count", is(2)))
                .andExpect(view().name("classrooms/classrooms"));
    }

    @Test
    void shouldReturnSaveFormClassroomDTO() throws Exception {

        ResultActions response = mockMvc.perform(get("/classrooms/new")
                .sessionAttr("newClassroom", ClassroomDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(view().name("classrooms/formSaveClassroom"));
    }

    @Test
    void shouldReturnSavedClassroomDTO_whenSave() throws Exception {

        ClassroomDTO saveClassroom = new ClassroomDTO();
        saveClassroom.setNumber(145);

        ResultActions response = mockMvc.perform(post("/classrooms/saved")
                .contentType(MEDIA_TYPE)
                .param("number", String.valueOf(saveClassroom.getNumber()))
                .sessionAttr("newClassroom", new ClassroomDTO()));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("savedClassroom", hasProperty("id", is(1))))
                .andExpect(model().attribute("savedClassroom", hasProperty("number", is(saveClassroom.getNumber()))))
                .andExpect(view().name("classrooms/formSavedClassroom"));
    }

    @Test
    void shouldReturn400BadNoValid_whenSave() throws Exception {

        ResultActions response = mockMvc.perform(post("/classrooms/saved")
                .contentType(MEDIA_TYPE)
                .param("number", "1445")
                .sessionAttr("newClassroom", new ClassroomDTO()));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUpdateFormWithFullFieldsClassroomWithId() throws Exception {

        ClassroomDTO updateClassroom = createClassroomDTO();

        ResultActions response = mockMvc.perform(get("/classrooms/{id}/update", updateClassroom.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("classroom", hasProperty("id", is(updateClassroom.getId()))))
                .andExpect(model().attribute("classroom", hasProperty("number", is(updateClassroom.getNumber()))))
                .andExpect(view().name("classrooms/formUpdateClassroom"));
    }

    @Test
    void shouldReturnUpdatedClassroomDTO_whenUpdate() throws Exception {

        ClassroomDTO updateClassroom = createClassroomDTO();

        ResultActions response = mockMvc.perform(patch("/classrooms/{id}/updated", updateClassroom.getId())
                        .contentType(MEDIA_TYPE)
                .param("number", "65")
                .sessionAttr("classroom", ClassroomDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("updatedClassroom", hasProperty("id", is(updateClassroom.getId()))))
                .andExpect(model().attribute("updatedClassroom", hasProperty("number", is(65))))
                .andExpect(view().name("classrooms/formUpdatedClassroom"));
    }

    @Test
    void shouldReturn404NotFound_whenUpdate() throws Exception {

        ResultActions response = mockMvc.perform(patch("/classrooms/{id}/updated", ID_NOT_EXISTS)
                .contentType(MEDIA_TYPE)
                .param("number", "65")
                .sessionAttr("classroom", ClassroomDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400NotValid_whenUpdate() throws Exception {

        ClassroomDTO updateClassroom = createClassroomDTO();

        ResultActions response = mockMvc.perform(patch("/classrooms/{id}/updated", updateClassroom.getId())
                .contentType(MEDIA_TYPE)
                .param("number", "4545")
                .sessionAttr("classroom", ClassroomDTO.class));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteById_thenReturn200() throws Exception {

        ClassroomDTO classroomDTO = createClassroomDTO();

        ResultActions response = mockMvc.perform(delete("/classrooms/{id}/deleted", classroomDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("classrooms/formDeletedClassroom"));
    }

    @Test
    void shouldReturn404NotFound_whenDeleteById() throws Exception {

        ResultActions response = mockMvc.perform(delete("/classrooms/{id}/deleted", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnFindByNumberForm() throws Exception {

        ResultActions response = mockMvc.perform(get("/classrooms/find/by_number")
                .sessionAttr("classroom", ClassroomDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("classrooms/formFindClassroomByNumber"));
    }

    @Test
    void shouldReturnClassroomDTO_whenFindByNumber() throws Exception {

        ClassroomDTO classroomDTO = createClassroomDTO();

        ResultActions response = mockMvc.perform(get("/classrooms/found/by_number")
                        .contentType(MEDIA_TYPE)
                .param("number", "145")
                .sessionAttr("classroom", ClassroomDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("classrooms", hasProperty("id", is(classroomDTO.getId()))))
                .andExpect(model().attribute("classrooms", hasProperty("number", is(classroomDTO.getNumber()))))
                .andExpect(view().name("classrooms/classrooms"));
    }

    @Test
    void shouldReturn404NotValid_whenFindByNumber() throws Exception {

        ResultActions response = mockMvc.perform(get("/classrooms/found/by_number")
                .contentType(MEDIA_TYPE)
                .param("number", "4545")
                .sessionAttr("classroom", ClassroomDTO.class));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteAll_thenReturn200() throws Exception {

        createClassroomsDTO();

        ResultActions response = mockMvc.perform(delete("/classrooms/deleted/all"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("classrooms/formDeletedAllClassrooms"));
    }

    private List<ClassroomDTO> createClassroomsDTO() {
        ClassroomDTO classroomTest = new ClassroomDTO();
        classroomTest.setNumber(45);
        ClassroomDTO classroomTest2 = new ClassroomDTO();
        classroomTest2.setNumber(5);
        List<ClassroomDTO> classroomDTOS = new ArrayList<>();
        classroomDTOS.add(classroomTest);
        classroomDTOS.add(classroomTest2);
        return classroomDTOS.stream()
                .map(classroomDTO -> service.save(classroomDTO)).collect(Collectors.toList());
    }

    private ClassroomDTO createClassroomDTO() {
        ClassroomDTO classroomTest = new ClassroomDTO();
        classroomTest.setNumber(145);
        return service.save(classroomTest);
    }
}