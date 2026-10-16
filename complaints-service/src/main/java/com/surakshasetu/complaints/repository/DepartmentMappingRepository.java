
/*
 * DepartmentMappingRepository
 *
 * Purpose:
 * Retrieves the fixed faculty ID assigned to a department
 * from the departments table.
 *
 * Working:
 * 1. Accepts a department ID.
 * 2. Fetches fixed_faculty_id from the database.
 * 3. Returns the faculty ID if a mapping exists.
 * 4. Returns an empty result if no mapping exists.
 */
package com.surakshasetu.complaints.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DepartmentMappingRepository {

    private final JdbcTemplate jdbcTemplate;

    public DepartmentMappingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Long> findFixedFacultyId(Long departmentId) {
        return jdbcTemplate.query(
                "SELECT fixed_faculty_id FROM departments WHERE id = ?",
                rs -> {
                    if (rs.next()) {
                        long facultyId = rs.getLong("fixed_faculty_id");

                        if (!rs.wasNull()) {
                            return Optional.of(facultyId);
                        }
                    }

                    return Optional.empty();
                },
                departmentId
        );
    }
}
