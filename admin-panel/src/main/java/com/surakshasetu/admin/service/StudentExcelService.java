package com.surakshasetu.admin.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

public interface StudentExcelService {

    ByteArrayResource downloadTemplate();

    void importStudents(MultipartFile file,
                        Long departmentId,
                        Long courseId);

}