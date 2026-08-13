package com.surakshasetu.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentLoginResponse {

    private boolean success;

    private String message;

    private Long studentId;

    private String enrollmentNo;

    private String studentName;
}