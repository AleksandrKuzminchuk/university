package ua.foxminded.task10.uml.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.service.StudentService;
import ua.foxminded.task10.uml.util.GroupValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/groups")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GroupController {


    GroupService groupService;
    StudentService studentService;

    GroupValidator groupValidator;

    @GetMapping()
    public String findAllGroups(Model model){
        log.info("requested-> [GET]-'/groups'");
        List<Group> groups = groupService.findAll();
        model.addAttribute("groups", groups);
        model.addAttribute("count", groups.size());
        log.info("FOUND {} groups", groups.size());
        return "groups/groups";
    }

    @GetMapping("/new")
    public String createFormForSaveGroup(@ModelAttribute("newGroup") Group group){
        log.info("requested-> [GET]-'/new'");
        return "groups/formSaveGroup";
    }

    @PostMapping("/saved")
    public String saveGroup(Model model, @ModelAttribute("newGroup") @Valid Group group, BindingResult bindingResult){
        log.info("requested-> [POST]-'/saved'");
        groupValidator.validate(group, bindingResult);
        if (bindingResult.hasErrors()){
            return "groups/formSaveGroup";
        }
        Group newGroup = groupService.save(group);
        model.addAttribute("newGroup", newGroup);
        log.info("SAVED {} SUCCESSFULLY", newGroup);
        return "groups/formSavedGroup";
    }

    @GetMapping("{groupId}/update")
    public String createFormForUpdateGroup(Model model, @PathVariable("groupId") Integer groupId){
        log.info("requested-> [GET]-'{groupId}/update'");
        Group group = groupService.findById(groupId);
        model.addAttribute("group", group);
        log.info("UPDATING... {}", group);
        return "groups/formUpdateGroup";
    }

    @PatchMapping("{groupId}/updated")
    public String updateGroup(Model model, @ModelAttribute @Valid Group group, BindingResult bindingResult,
                              @PathVariable("groupId") Integer groupId){
        log.info("requested-> [PATCH]-'/{groupId}/updated'");
        groupValidator.validate(group, bindingResult);
        if (bindingResult.hasErrors()){
            return "groups/formUpdateGroup";
        }
        groupService.updateGroup(groupId, group);
        model.addAttribute("updatedGroup", group);
        log.info("UPDATED {} SUCCESSFULLY", group);
        return "groups/formUpdatedGroup";
    }

    @DeleteMapping("{groupId}/deleted")
    public String deleteGroupById(Model model, @PathVariable("groupId") Integer groupId){
        log.info("requested-> [DELETE]-'/{groupId}/deleted'");
        Group group = groupService.findById(groupId);
        groupService.deleteById(groupId);
        model.addAttribute("deleteGroup", group);
        log.info("DELETED GROUP BY ID - {} SUCCESSFULLY", groupId);
        return "groups/formDeletedGroup";
    }

    @DeleteMapping("/delete/all")
    public String deleteAllGroups(Model model){
        log.info("requested-> [DELETE]-'/delete/all'");
        Long countGroups = groupService.count();
        groupService.deleteAll();
        model.addAttribute("groups", countGroups);
        log.info("DELETED ALL GROUPS SUCCESSFULLY");
        return "groups/formDeletedAllGroups";
    }

    @GetMapping("find/by_name")
    public String createFormForFindGroupByName(@ModelAttribute("group") Group group){
        log.info("requested-> [GET]-'find/by_name'");
        return "groups/formForFindGroupByName";
    }

    @GetMapping("found/by_name")
    public String findGroupByName(Model model, @ModelAttribute @Valid Group group, BindingResult bindingResult){
        log.info("requested-> [GET]-'found/by_name'");
        if (bindingResult.hasErrors()){
            return "groups/formForFindGroupByName";
        }
        Group result = groupService.findGroupByName(group);
        model.addAttribute("groups", result);
        log.info("FOUND {} GROUPS BY NAME {} SUCCESSFULLY", result, group.getName());
        return "groups/groups";
    }

    @GetMapping("{groupId}/found/students")
    public String findStudentsByGroupId(Model model, @PathVariable("groupId") Integer groupId){
        log.info("requested-> [GET]-'{groupId}/found/students'");
        Group group = groupService.findById(groupId);
        List<Student> students = studentService.findByGroupId(groupId);
        model.addAttribute("students", students);
        model.addAttribute("group", group);
        log.info("FOUND {} STUDENTS BY GROUP ID - {} SUCCESSFULLY", students.size(), groupId);
        return "groups/formForFoundStudentsByGroupId";
    }
}
