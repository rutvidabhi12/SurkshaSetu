package com.surakshasetu.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentLoginRequest {

    private String enrollmentNo;

    private String password;
}