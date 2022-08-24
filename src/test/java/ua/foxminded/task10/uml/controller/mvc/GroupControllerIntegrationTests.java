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
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.task10.uml.util.ConstantsTests.ID_NOT_EXISTS;
import static ua.foxminded.task10.uml.util.MediaTypeTest.MEDIA_TYPE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(value = {"classpath:create-table-students.sql", "classpath:create-table-groups.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GroupControllerIntegrationTests {

    @Autowired
    private GroupService service;
    @Autowired
    private StudentService studentService;
    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        studentService.deleteAll();
        service.deleteAll();
    }

    @AfterEach
    void tearDown() {
        studentService.deleteAll();
        service.deleteAll();
    }

    @Test
    void shouldReturnGroupsDTOList_whenFindAll() throws Exception {

        List<GroupDTO> groupsDTO = createGroupsDTO();

        ResultActions response = mockMvc.perform(get("/groups"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("groups", groupsDTO))
                .andExpect(model().attribute("groups", hasSize(2)))
                .andExpect(model().attribute("groups", hasItem(
                        allOf(
                                hasProperty("id", is(1)),
                                hasProperty("name", is("G-10"))
                        )
                )))
                .andExpect(model().attribute("groups", hasItem(
                        allOf(
                                hasProperty("id", is(2)),
                                hasProperty("name", is("G-15"))
                        )
                )))
                .andExpect(model().attribute("count", is(2)))
                .andExpect(view().name("groups/groups"));
    }

    @Test
    void shouldReturnSaveForm() throws Exception {

        ResultActions response = mockMvc.perform(get("/groups/new")
                .contentType(MEDIA_TYPE)
                .sessionAttr("newGroup", GroupDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(view().name("groups/formSaveGroup"));
    }

    @Test
    void shouldReturnGroupDTO_whenSave() throws Exception {

        GroupDTO savedGroup = new GroupDTO("G-10");

        ResultActions response = mockMvc.perform(post("/groups/saved")
                .contentType(MEDIA_TYPE)
                .param("name", String.valueOf(savedGroup.getName()))
                .sessionAttr("newGroup", GroupDTO.class));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("newGroup", hasProperty("id", is(1))))
                .andExpect(model().attribute("newGroup", hasProperty("name", is(savedGroup.getName()))))
                .andExpect(view().name("groups/formSavedGroup"));
    }

    @Test
    void shouldReturn400NotValid_whenSave() throws Exception {

        GroupDTO savedGroup = new GroupDTO("G-1045");

        ResultActions response = mockMvc.perform(post("/groups/saved")
                .contentType(MEDIA_TYPE)
                .param("name", String.valueOf(savedGroup.getName()))
                .sessionAttr("newGroup", GroupDTO.class));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUpdateFrom() throws Exception {

        GroupDTO groupDTO = createGroupDTO();

        ResultActions response = mockMvc.perform(get("/groups/{id}/update", groupDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("group", hasProperty("id", is(groupDTO.getId()))))
                .andExpect(model().attribute("group", hasProperty("name", is(groupDTO.getName()))))
                .andExpect(view().name("groups/formUpdateGroup"));
    }

    @Test
    void shouldReturn404NotFound_whenUpdateForm() throws Exception {

        ResultActions response = mockMvc.perform(get("/groups/{id}/update", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedGroupDTO_whenUpdate() throws Exception {

        GroupDTO groupDTO = createGroupDTO();

        ResultActions response = mockMvc.perform(patch("/groups/{id}/updated", groupDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("name", "G-15")
                .sessionAttr("updateGroup", GroupDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("updatedGroup", hasProperty("id", is(1))))
                .andExpect(model().attribute("updatedGroup", hasProperty("name", is("G-15"))))
                .andExpect(view().name("groups/formUpdatedGroup"));
    }

    @Test
    void shouldReturn400NotValid_whenUpdate() throws Exception {

        GroupDTO groupDTO = createGroupDTO();

        ResultActions response = mockMvc.perform(patch("/groups/{id}/updated", groupDTO.getId())
                .contentType(MEDIA_TYPE)
                .param("name", "G-1545")
                .sessionAttr("updateGroup", GroupDTO.class));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404NotFound_whenUpdate() throws Exception {

        ResultActions response = mockMvc.perform(patch("/groups/{id}/updated", ID_NOT_EXISTS)
                .contentType(MEDIA_TYPE)
                .param("name", "G-15")
                .sessionAttr("updateGroup", GroupDTO.class));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200_whenDeleteById() throws Exception {

        GroupDTO groupDTO = createGroupDTO();

        ResultActions response = mockMvc.perform(delete("/groups/{id}/deleted", groupDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("groups/formDeletedGroup"));
    }

    @Test
    void shouldReturn404NotFound_whenDeleteById() throws Exception {

        ResultActions response = mockMvc.perform(delete("/groups/{id}/deleted", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200_whenDeleteAll() throws Exception {

        createGroupsDTO();

        ResultActions response = mockMvc.perform(delete("/groups/delete/all"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("groups/formDeletedAllGroups"));
    }

    @Test
    void shouldReturnFindByNameForm() throws Exception {

        ResultActions response = mockMvc.perform(get("/groups/find/by_name")
                .contentType(MEDIA_TYPE)
                .sessionAttr("grpup", GroupDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(view().name("groups/formForFindGroupByName"));
    }

    @Test
    void shouldReturnGroupDTO_whenFindByName() throws Exception {

        GroupDTO groupDTO = createGroupDTO();

        ResultActions response = mockMvc.perform(get("/groups/found/by_name")
                .contentType(MEDIA_TYPE)
                .param("name", "G-10")
                .sessionAttr("findGroup", GroupDTO.class));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("groups", hasProperty("id", is(groupDTO.getId()))))
                .andExpect(model().attribute("groups", hasProperty("name", is(groupDTO.getName()))))
                .andExpect(view().name("groups/groups"));
    }

    @Test
    void shouldReturn400NotValid_whenFindByName() throws Exception {

        ResultActions response = mockMvc.perform(get("/groups/found/by_name")
                .contentType(MEDIA_TYPE)
                .param("name", "G-4545")
                .sessionAttr("findGroup", GroupDTO.class));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnStudentsDTOList_whenFindStudents() throws Exception {

        GroupDTO groupDTO = createGroupDTO();

        List<StudentDTO> studentsDTO = createStudentsDTO();

        studentsDTO.stream().peek(studentDTO -> studentDTO.setGroup(groupDTO))
                .forEach(studentDTO -> studentService.update(studentDTO));

        ResultActions response = mockMvc.perform(get("/groups/{id}/found/students", groupDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MEDIA_TYPE))
                .andExpect(model().attribute("students", hasSize(2)))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                                hasProperty("id", is(studentsDTO.get(0).getId())),
                                hasProperty("firstName", is(studentsDTO.get(0).getFirstName())),
                                hasProperty("lastName", is(studentsDTO.get(0).getLastName())),
                                hasProperty("course", is(studentsDTO.get(0).getCourse())),
                                hasProperty("group", is(groupDTO))
                        )
                )))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                                hasProperty("id", is(studentsDTO.get(1).getId())),
                                hasProperty("firstName", is(studentsDTO.get(1).getFirstName())),
                                hasProperty("lastName", is(studentsDTO.get(1).getLastName())),
                                hasProperty("course", is(studentsDTO.get(1).getCourse())),
                                hasProperty("group", is(groupDTO))
                        )
                )))
                .andExpect(view().name("groups/formForFoundStudentsByGroupId"));
    }

    @Test
    void shouldReturn404NotFound_whenFindStudents() throws Exception {

        ResultActions response = mockMvc.perform(get("/groups/{id}/found/students", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    private List<GroupDTO> createGroupsDTO() {
        List<GroupDTO> groupsDTO = new ArrayList<>();
        GroupDTO groupDTOTest = new GroupDTO();
        groupDTOTest.setName("G-10");
        GroupDTO groupDTOTest2 = new GroupDTO();
        groupDTOTest2.setName("G-15");
        groupsDTO.add(groupDTOTest);
        groupsDTO.add(groupDTOTest2);
        return groupsDTO.stream().map(groupDTO -> service.save(groupDTO)).collect(Collectors.toList());
    }

    private GroupDTO createGroupDTO() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("G-10");
        return service.save(groupDTO);
    }

    private List<StudentDTO> createStudentsDTO() {
        List<StudentDTO> studentDTOS = new ArrayList<>();
        studentDTOS.add(new StudentDTO("Mark", "Oliver", 5));
        studentDTOS.add(new StudentDTO("Mark", "Humek", 5));
        return studentDTOS.stream().
                map(studentDTO1 -> studentService.save(studentDTO1)).collect(Collectors.toList());
    }
}