package com.surakshasetu.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.surakshasetu.admin.entity.Semester;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {

    List<Semester> findByCourseId(Long courseId);

}