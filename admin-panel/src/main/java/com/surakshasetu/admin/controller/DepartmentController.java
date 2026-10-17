package com.surakshasetu.admin.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.surakshasetu.admin.entity.Department;
import com.surakshasetu.admin.service.DepartmentService;

@Controller
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/departments")
    public String departmentList(Model model) {

        model.addAttribute("departments",
                departmentService.getAllDepartments());

        return "department/index";
    }

    @GetMapping("/departments/add")
    public String addDepartmentForm(Model model) {

        model.addAttribute("department", new Department());

        return "department/add";
    }

    @PostMapping("/departments/save")
    public String saveDepartment(@ModelAttribute Department department,
                                Model model) {

        try {

            departmentService.saveDepartment(department);

            return "redirect:/departments?success";

        } catch (RuntimeException e) {

            model.addAttribute("department", department);
            model.addAttribute("error", e.getMessage());

            return "department/add";
        }
    }

    @GetMapping("/departments/edit/{id}")
    public String editDepartment(@PathVariable Long id, Model model) {

        Department department = departmentService
                .getDepartmentById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        model.addAttribute("department", department);

        return "department/edit";
    }

    @PostMapping("/departments/update")
    public String updateDepartment(@ModelAttribute Department department,
                                Model model) {

        try {

            departmentService.updateDepartment(
                    department.getId(),
                    department
            );

            return "redirect:/departments?updated";

        } catch (RuntimeException e) {

            model.addAttribute("department", department);
            model.addAttribute("error", e.getMessage());

            return "department/edit";
        }
    }

    @GetMapping("/departments/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {

        departmentService.deleteDepartment(id);

        return "redirect:/departments?deleted";
    }

}