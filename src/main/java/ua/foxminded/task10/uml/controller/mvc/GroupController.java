package ua.foxminded.task10.uml.controller.mvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.dto.GroupDTO;
import ua.foxminded.task10.uml.dto.StudentDTO;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.service.StudentService;
import ua.foxminded.task10.uml.util.validations.GroupValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {


    private final GroupService groupService;
    private final StudentService studentService;
    private final GroupValidator groupValidator;

    @GetMapping
    public String findAll(Model model){
        log.info("requested-> [GET]-'/groups'");
        List<GroupDTO> groups = groupService.findAll();
        model.addAttribute("groups", groups);
        model.addAttribute("count", groups.size());
        log.info("FOUND {} groups", groups.size());
        return "groups/groups";
    }

    @GetMapping("/new")
    public String saveForm(@ModelAttribute("newGroup") GroupDTO groupDTO){
        log.info("requested-> [GET]-'/groups/new'");
        return "groups/formSaveGroup";
    }

    @PostMapping("/saved")
    public String save(Model model, @ModelAttribute("newGroup") @Valid GroupDTO groupDTO, BindingResult bindingResult){
        log.info("requested-> [POST]-'/groups/saved'");
        groupValidator.validate(groupDTO, bindingResult);
        if (bindingResult.hasErrors()){
            return "groups/formSaveGroup";
        }
        GroupDTO newGroup = groupService.save(groupDTO);
        model.addAttribute("newGroup", newGroup);
        log.info("SAVED {} SUCCESSFULLY", newGroup);
        return "groups/formSavedGroup";
    }

    @GetMapping("/{id}/update")
    public String updateFrom(Model model, @PathVariable("id") Integer id){
        log.info("requested-> [GET]-/groups'/groups/{id}/update'");
        GroupDTO group = groupService.findById(id);
        model.addAttribute("group", group);
        log.info("UPDATING... {}", group);
        return "groups/formUpdateGroup";
    }

    @PatchMapping("/{id}/updated")
    public String update(Model model, @ModelAttribute @Valid GroupDTO groupDTO, BindingResult bindingResult,
                              @PathVariable("id") Integer id){
        log.info("requested-> [PATCH]-'/groups/{id}/updated'");
        groupValidator.validate(groupDTO, bindingResult);
        if (bindingResult.hasErrors()){
            return "groups/formUpdateGroup";
        }
        groupDTO.setId(id);
        GroupDTO updatedGroup = groupService.update(groupDTO);
        model.addAttribute("updatedGroup", updatedGroup);
        log.info("UPDATED {} SUCCESSFULLY", updatedGroup);
        return "groups/formUpdatedGroup";
    }

    @DeleteMapping("/{id}/deleted")
    public String deleteById(Model model, @PathVariable("id") Integer id){
        log.info("requested-> [DELETE]-'/groups/{id}/deleted'");
        GroupDTO groupDTO = groupService.findById(id);
        groupService.deleteById(id);
        model.addAttribute("deleteGroup", groupDTO);
        log.info("DELETED GROUP BY ID - {} SUCCESSFULLY", id);
        return "groups/formDeletedGroup";
    }

    @DeleteMapping("/delete/all")
    public String deleteAll(Model model){
        log.info("requested-> [DELETE]-'/groups/delete/all'");
        Long countGroups = groupService.count();
        groupService.deleteAll();
        model.addAttribute("groups", countGroups);
        log.info("DELETED ALL GROUPS SUCCESSFULLY");
        return "groups/formDeletedAllGroups";
    }

    @GetMapping("/find/by_name")
    public String findByNameForm(@ModelAttribute("group") GroupDTO groupDTO){
        log.info("requested-> [GET]-'find/by_name'");
        return "groups/formForFindGroupByName";
    }

    @GetMapping("/found/by_name")
    public String findByName(Model model, @ModelAttribute @Valid GroupDTO groupDTO, BindingResult bindingResult){
        log.info("requested-> [GET]-'/groups/found/by_name'");
        if (bindingResult.hasErrors()){
            return "groups/formForFindGroupByName";
        }
        GroupDTO result = groupService.findByName(groupDTO.getName());
        model.addAttribute("groups", result);
        log.info("FOUND {} GROUP BY NAME {} SUCCESSFULLY", result, groupDTO.getName());
        return "groups/groups";
    }

    @GetMapping("/{id}/found/students")
    public String findStudents(Model model, @PathVariable("id") Integer id){
        log.info("requested-> [GET]-'/groups/{id}/found/students'");
        GroupDTO group = groupService.findById(id);
        List<StudentDTO> studentsDTO = studentService.findByGroupId(id);
        model.addAttribute("students", studentsDTO);
        model.addAttribute("group", group);
        log.info("FOUND {} STUDENTS BY GROUP ID - {} SUCCESSFULLY", studentsDTO.size(), id);
        return "groups/formForFoundStudentsByGroupId";
    }
}
