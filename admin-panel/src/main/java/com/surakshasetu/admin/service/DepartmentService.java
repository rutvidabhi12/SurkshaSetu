package com.surakshasetu.admin.service;

import com.surakshasetu.admin.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    Department saveDepartment(Department department);

    List<Department> getAllDepartments();

    Optional<Department> getDepartmentById(Long id);

    Department updateDepartment(Long id, Department department);

    void deleteDepartment(Long id);

}