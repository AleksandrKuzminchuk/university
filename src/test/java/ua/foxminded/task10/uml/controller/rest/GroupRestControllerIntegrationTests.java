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
import ua.foxminded.task10.uml.dto.GroupCreateDTO;
import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.dto.StudentDTO;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.service.StudentService;

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
@Sql(value = {"classpath:create-table-students.sql", "classpath:create-table-groups.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GroupRestControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GroupService service;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ObjectMapper mapper;

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
    void givenGroupsDTOList_whenFindAll_ThenReturnGroupsDTOList() throws Exception {

        createGroupsDTO();

        ResultActions response = mockMvc.perform(get("/api/groups"));

        response.andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groups.size()", is(2)));
    }

    @Test
    void givenGroupDTOObject_whenCreateWithNewName_thenReturnGroupDTOObject() throws Exception {

        GroupCreateDTO groupCreateDTO = new GroupCreateDTO();
        groupCreateDTO.setName("G-10");

        ResultActions response = mockMvc.perform(post("/api/groups/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(groupCreateDTO)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(groupCreateDTO.getName())));
    }

    @Test
    void givenGroupDTOObject_whenCreateWithNewName_thenReturn400NotValid() throws Exception {

        GroupCreateDTO groupCreateDTO = new GroupCreateDTO();
        groupCreateDTO.setName("k-78");

        ResultActions response = mockMvc.perform(post("/api/groups/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(groupCreateDTO)));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenUpdatedGroupDTOObject_whenUpdate_thenReturnUpdatedGroupDTOObject() throws Exception {

        GroupDTO groupDTO = createGroupDTO();

        GroupCreateDTO updateGroupDTO = new GroupCreateDTO();
        updateGroupDTO.setName("H-858");

        ResultActions response = mockMvc.perform(patch("/api/groups/update/{id}", groupDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateGroupDTO)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(groupDTO.getId())))
                .andExpect(jsonPath("$.name", is(updateGroupDTO.getName())));
    }

    @Test
    void givenUpdatedGroupDTOObject_whenUpdate_thenReturn400NotValid() throws Exception {

        GroupDTO groupDTO = createGroupDTO();

        GroupCreateDTO updateGroupDTO = new GroupCreateDTO();
        updateGroupDTO.setName("h-78");

        ResultActions response = mockMvc.perform(patch("/api/groups/update/{id}", groupDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateGroupDTO)));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenResponseEntity_whenDeleteById_thenReturn200() throws Exception {

        GroupDTO groupDTO = createGroupDTO();

        ResultActions response = mockMvc.perform(delete("/api/groups/{id}/delete", groupDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenResponseEntity_whenDeleteById_thenReturn404NotFound() throws Exception {

        ResultActions response = mockMvc.perform(delete("/api/groups/{id}/delete", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenResponseBody_whenDeleteAll_thenReturn200() throws Exception {

        createGroupsDTO();

        ResultActions response = mockMvc.perform(delete("/api/groups/delete/all"));

        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenGroupDTOObject_whenFindByName_thenReturnGroupDTOObject() throws Exception {

        GroupDTO groupDTO = createGroupDTO();

        HttpHeaders headers = new HttpHeaders();
        headers.set("name", "G-10");

        ResultActions response = mockMvc.perform(get("/api/groups//find/by_name")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(groupDTO.getId())))
                .andExpect(jsonPath("$.name", is(groupDTO.getName())));
    }

    @Test
    void givenGroupDTOObject_whenFindByName_thenReturn404NotFound() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.set("name", "G-10");

        ResultActions response = mockMvc.perform(get("/api/groups//find/by_name")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenStudentsDTOList_whenFindStudents_thenReturnStudentsDTOList() throws Exception {

        GroupDTO groupDTO = createGroupDTO();

        createStudentsDTO().stream().peek(studentDTO -> studentDTO.setGroup(groupDTO))
                .forEach(studentDTO -> studentService.update(studentDTO));

        ResultActions response = mockMvc.perform(get("/api/groups/{id}/find/students", groupDTO.getId()));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.students.size()", is(2)));
    }

    @Test
    void givenStudentsDTOList_whenFindStudents_thenReturn404NotFound() throws Exception {

        ResultActions response = mockMvc.perform(get("/api/groups/{id}/find/students", ID_NOT_EXISTS));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    private List<GroupDTO> createGroupsDTO(){
        List<GroupDTO> groupsDTO = new ArrayList<>();
        GroupDTO groupDTOTest = new GroupDTO();
        groupDTOTest.setName("G-10");
        GroupDTO groupDTOTest2 = new GroupDTO();
        groupDTOTest2.setName("H-10");
        groupsDTO.add(groupDTOTest);
        groupsDTO.add(groupDTOTest2);
        return groupsDTO.stream().map(groupDTO -> service.save(groupDTO)).collect(Collectors.toList());
    }

    private GroupDTO createGroupDTO(){
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName("G-10");
        return service.save(groupDTO);
    }

    private List<StudentDTO> createStudentsDTO(){
        List<StudentDTO> studentDTOS = new ArrayList<>();
        studentDTOS.add(new StudentDTO("Mark", "Oliver", 5));
        studentDTOS.add(new StudentDTO("Mark", "Humek", 5));
        return studentDTOS.stream().
                map(studentDTO1 -> studentService.save(studentDTO1)).collect(Collectors.toList());
    }
}