package com.surakshasetu.admin.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.surakshasetu.admin.entity.Department;
import com.surakshasetu.admin.repository.DepartmentRepository;
import com.surakshasetu.admin.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department saveDepartment(Department department) {

        if (departmentRepository.existsByDepartmentName(department.getDepartmentName())) {

            throw new RuntimeException("Department Name already exists.");

        }

        if (departmentRepository.existsByDepartmentCode(department.getDepartmentCode())) {

            throw new RuntimeException("Department Code already exists.");

        }

        return departmentRepository.save(department);

}

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public Department updateDepartment(Long id, Department department) {

        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        if (!existingDepartment.getDepartmentName().equalsIgnoreCase(department.getDepartmentName())
                && departmentRepository.existsByDepartmentName(department.getDepartmentName())) {

            throw new RuntimeException("Department Name already exists.");

        }

        if (!existingDepartment.getDepartmentCode().equalsIgnoreCase(department.getDepartmentCode())
                && departmentRepository.existsByDepartmentCode(department.getDepartmentCode())) {

            throw new RuntimeException("Department Code already exists.");

        }

        existingDepartment.setDepartmentName(department.getDepartmentName());
        existingDepartment.setDepartmentCode(department.getDepartmentCode());
        existingDepartment.setActive(department.isActive());

        return departmentRepository.save(existingDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}