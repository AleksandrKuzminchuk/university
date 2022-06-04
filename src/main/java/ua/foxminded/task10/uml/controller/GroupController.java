package ua.foxminded.task10.uml.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.task10.uml.model.Group;
import ua.foxminded.task10.uml.model.Student;
import ua.foxminded.task10.uml.service.GroupService;
import ua.foxminded.task10.uml.service.StudentService;

import java.util.List;

@Controller
@RequestMapping("/")
public class GroupController {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    private final GroupService groupService;
    private final StudentService studentService;

    @Autowired
    public GroupController(GroupService groupService, StudentService studentService) {
        this.groupService = groupService;
        this.studentService = studentService;
    }

    @GetMapping("groups")
    @ResponseStatus(HttpStatus.OK)
    public String findAllGroups(Model model){
        logger.info("requested-> [GET]-'groups'");
        List<Group> groups = groupService.findAll();
        model.addAttribute("groups", groups);
        model.addAttribute("count", groups.size());
        logger.info("FOUND {} groups", groups.size());
        return "groups/groups";
    }

    @GetMapping("new_group")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForSaveGroup(@ModelAttribute("newGroup") Group group){
        logger.info("requested-> [GET]-'/new_group'");
        return "groups/formSaveGroup";
    }

    @PostMapping("saved_group")
    @ResponseStatus(HttpStatus.OK)
    public String saveGroup(Model model, @ModelAttribute Group group){
        logger.info("requested-> [POST]-'saved_group'");
        Group newGroup = groupService.save(group);
        model.addAttribute("newGroup", newGroup);
        logger.info("SAVED {} SUCCESSFULLY", newGroup);
        return "groups/formSavedGroup";
    }

    @GetMapping("{groupId}/update_group")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForUpdateGroup(Model model, @PathVariable("groupId") Integer groupId){
        logger.info("requested-> [GET]-'{id}/update_group'");
        Group group = groupService.findById(groupId);
        model.addAttribute("group", group);
        logger.info("UPDATING... {}", group);
        return "groups/formUpdateGroup";
    }

    @PatchMapping("{groupId}/updated_group")
    @ResponseStatus(HttpStatus.OK)
    public String updateGroup(Model model, @ModelAttribute Group group, @PathVariable("groupId") Integer groupId){
        logger.info("requested-> [PATCH]-'/{id}/updated_group'");
        groupService.updateGroup(groupId, group);
        model.addAttribute("updatedGroup", group);
        logger.info("UPDATED {} SUCCESSFULLY", group);
        return "groups/formUpdatedGroup";
    }

    @DeleteMapping("{groupId}/delete_group")
    @ResponseStatus(HttpStatus.OK)
    public String deleteGroupById(Model model, @PathVariable("groupId") Integer groupId){
        logger.info("requested-> [DELETE]-'/{id}/delete_group'");
        Group group = groupService.findById(groupId);
        groupService.deleteById(groupId);
        model.addAttribute("deleteGroup", group);
        logger.info("DELETED GROUP BY ID - {} SUCCESSFULLY", groupId);
        return "groups/formDeletedGroup";
    }

    @DeleteMapping("delete_all_groups")
    @ResponseStatus(HttpStatus.OK)
    public String deleteAllGroups(){
        logger.info("requested-> [DELETE]-'/delete_all_groups'");
        groupService.deleteAll();
        logger.info("DELETED ALL GROUPS SUCCESSFULLY");
        return "groups/formDeletedAllGroups";
    }

    @GetMapping("delete_group_by_name")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForDeleteGroupByName(@ModelAttribute("group") Group group){
        logger.info("requested-> [GET]-'delete_group_by_name'");
        return "groups/formForDeleteGroupByName";
    }

    @DeleteMapping("deleted_group_by_name")
    @ResponseStatus(HttpStatus.OK)
    public String deleteGroupByName(Model model, @ModelAttribute Group group){
        logger.info("requested-> [DELETE]-'deleted_group_by_name'");
        groupService.delete(group);
        model.addAttribute("deleteGroupByName", group);
        logger.info("DELETED GROUP BY NAME {} SUCCESSFULLY", group.getName());
        return "groups/formDeletedGroupByName";
    }

    @GetMapping("find_group_by_name")
    @ResponseStatus(HttpStatus.OK)
    public String createFormForFindGroupByName(@ModelAttribute("group") Group group){
        logger.info("requested-> [GET]-'find_group_by_name'");
        return "groups/formForFindGroupByName";
    }

    @GetMapping("found_group_by_name")
    @ResponseStatus(HttpStatus.OK)
    public String findGroupByName(Model model, @ModelAttribute Group group){
        logger.info("requested-> [GET]-'found_group_by_name'");
        Group result = groupService.findByGroupName(group.getName());
        model.addAttribute("groups", result);
        logger.info("FOUND GROUP BY NAME {} SUCCESSFULLY", group.getName());
        return "groups/formFoundGroupByName";
    }

    @GetMapping("{groupId}/found_students_by_group")
    @ResponseStatus(HttpStatus.OK)
    public String findStudentsByGroupId(Model model, @PathVariable("groupId") Integer groupId){
        logger.info("requested-> [GET]-'{groupId}/found_students_by_group'");
        Group group = groupService.findById(groupId);
        List<Student> students = studentService.findStudentsByGroupName(group);
        model.addAttribute("students", students);
        model.addAttribute("group", group);
        return "groups/formForFoundStudentsByGroupId";
    }
}
