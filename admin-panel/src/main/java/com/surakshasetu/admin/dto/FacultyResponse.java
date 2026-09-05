package com.surakshasetu.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FacultyResponse {

    private Long id;

    private String facultyCode;

    private String firstName;
    private String middleName;
    private String lastName;

    private String department;
    private String designation;
    private String gender;

    private String mobile;
    private String email;

    private String photo;

    private boolean active;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public FacultyResponse() {
    }


    // ==========================================
    // ID
    // ==========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    // ==========================================
    // FACULTY CODE
    // ==========================================

    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String facultyCode) {
        this.facultyCode = facultyCode;
    }


    // ==========================================
    // FIRST NAME
    // ==========================================

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    // ==========================================
    // MIDDLE NAME
    // ==========================================

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }


    // ==========================================
    // LAST NAME
    // ==========================================

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    // ==========================================
    // DEPARTMENT
    // ==========================================

    /*
     * Faculty entity ma field nu naam "department" che.
     * DTO ma apde departmentName use kariye chhiye.
     *
     * @JsonProperty("department") thi JSON ma
     * "department" properly map thashe.
     */

    @JsonProperty("department")
    public String getDepartment() {
        return department;
    }

    @JsonProperty("department")
    public void setDepartment(String department) {
        this.department = department;
    }


    // ==========================================
    // DESIGNATION
    // ==========================================

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }


    // ==========================================
    // GENDER
    // ==========================================

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    // ==========================================
    // MOBILE
    // ==========================================

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    // ==========================================
    // EMAIL
    // ==========================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // ==========================================
    // PHOTO
    // ==========================================

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    // ==========================================
    // ACTIVE
    // ==========================================

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}