package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.ProjectService;
import com.cydeo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("project",new ProjectDTO());
        model.addAttribute("managers",userService.listAllByRole("manager"));
        model.addAttribute("projects",projectService.listAllProjects());
        return "/project/create";
    }

    @PostMapping("/create")
    public String insertProject(@Valid @ModelAttribute("project") ProjectDTO project, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("managers",userService.listAllByRole("manager"));
            model.addAttribute("projects",projectService.listAllProjects());
            return "/project/create";
        }
        projectService.save(project);
        return "redirect:/project/create";
    }

    @GetMapping("/delete/{projectCode}")
    public String deleteProject(@PathVariable("projectCode") String id){
        projectService.delete(id);
        return "redirect:/project/create";
    }

    @GetMapping("/complete/{projectCode}")
    public String completeProject(@PathVariable("projectCode") String id){
        projectService.complete(id);
        return "redirect:/project/create";
    }

    @GetMapping("/update/{projectCode}")
    public String editProject(@PathVariable("projectCode") String id, Model model){
        model.addAttribute("project",projectService.getByProjectCode(id));
        model.addAttribute("managers",userService.listAllByRole("manager"));
        model.addAttribute("projects",projectService.listAllProjects());
        return "/project/update";
    }

    @PostMapping("/update")
    public String updateProject(@Valid @ModelAttribute("project") ProjectDTO project, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("managers",userService.listAllByRole("manager"));
            model.addAttribute("projects",projectService.listAllProjects());
            return "/project/update";
        }
        projectService.update(project);
        return "redirect:/project/create";
    }

    @GetMapping("/manager/project-status")
    public String getProjectByManager(Model model){

        List<ProjectDTO> projects = projectService.listAllProjectDetails();

        model.addAttribute("projects",projects);
        return "/manager/project-status";
    }

    @GetMapping("/manager/complete/{id}")
    public String managerCompleteProject(@PathVariable("id") String id){
        projectService.complete(id);
        return "redirect:/project/manager/project-status";
    }

}
