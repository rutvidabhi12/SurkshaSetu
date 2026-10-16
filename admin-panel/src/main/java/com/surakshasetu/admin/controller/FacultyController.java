package com.surakshasetu.admin.controller;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.surakshasetu.admin.dto.FacultyResponse;
import com.surakshasetu.admin.service.DepartmentService;

@Controller
public class FacultyController {

    private final RestTemplate restTemplate;
    private final DepartmentService departmentService;

    public FacultyController(
            RestTemplate restTemplate,
            DepartmentService departmentService) {

        this.restTemplate = restTemplate;
        this.departmentService = departmentService;
    }


    // =====================================================
    // FACULTY LIST
    // =====================================================

    @GetMapping("/faculty")
    public String facultyList(Model model) {

        String url =
                "http://localhost:8082/api/faculty";

        FacultyResponse[] response =
                restTemplate.getForObject(
                        url,
                        FacultyResponse[].class
                );

        List<FacultyResponse> faculties =
                response != null
                        ? List.of(response)
                        : List.of();

        model.addAttribute(
                "faculties",
                faculties
        );

        model.addAttribute(
                "departments",
                departmentService.getAllDepartments()
        );

        return "faculty/index";
    }


    // =====================================================
    // ADD FACULTY PAGE
    // =====================================================

    @GetMapping("/faculty/add")
    public String addFaculty(Model model) {

        model.addAttribute(
                "departments",
                departmentService.getAllDepartments()
        );

        return "faculty/add";
    }


    // =====================================================
    // SAVE NEW FACULTY
    // =====================================================

    @PostMapping("/faculty/add")
    public String saveFaculty(

            @ModelAttribute FacultyResponse faculty,

            @RequestParam(
                    value = "photoFile",
                    required = false
            )
            MultipartFile photoFile,

            RedirectAttributes redirectAttributes) {

        try {

            // =============================================
            // SAVE FACULTY DATA
            // =============================================

            String url =
                    "http://localhost:8082/api/faculty";

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON
            );

            HttpEntity<FacultyResponse> request =
                    new HttpEntity<>(
                            faculty,
                            headers
                    );

            ResponseEntity<FacultyResponse> response =
                    restTemplate.postForEntity(
                            url,
                            request,
                            FacultyResponse.class
                    );


            if (!response.getStatusCode()
                    .is2xxSuccessful()) {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "Faculty add failed."
                );

                return "redirect:/faculty";
            }


            // =============================================
            // GET SAVED FACULTY ID
            // =============================================

            FacultyResponse savedFaculty =
                    response.getBody();


            if (savedFaculty == null ||
                savedFaculty.getId() == null) {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "Faculty saved but ID was not received."
                );

                return "redirect:/faculty";
            }


            // =============================================
            // PHOTO UPLOAD
            // =============================================

            if (photoFile != null &&
                !photoFile.isEmpty()) {

                String photoUrl =
                        "http://localhost:8082/api/faculty/"
                        + savedFaculty.getId()
                        + "/photo";


                HttpHeaders photoHeaders =
                        new HttpHeaders();

                photoHeaders.setContentType(
                        MediaType.MULTIPART_FORM_DATA
                );


                MultiValueMap<String, Object> body =
                        new LinkedMultiValueMap<>();


                ByteArrayResource resource =
                        new ByteArrayResource(
                                photoFile.getBytes()
                        ) {

                            @Override
                            public String getFilename() {

                                return photoFile
                                        .getOriginalFilename();
                            }
                        };


                body.add(
                        "photo",
                        resource
                );


                HttpEntity<MultiValueMap<String, Object>>
                        photoRequest =
                        new HttpEntity<>(
                                body,
                                photoHeaders
                        );


                ResponseEntity<FacultyResponse>
                        photoResponse =
                        restTemplate.postForEntity(
                                photoUrl,
                                photoRequest,
                                FacultyResponse.class
                        );


                if (!photoResponse.getStatusCode()
                        .is2xxSuccessful()) {

                    redirectAttributes.addFlashAttribute(
                            "warning",
                            "Faculty added, but photo upload failed."
                    );

                    return "redirect:/faculty";
                }
            }


            // =============================================
            // SUCCESS
            // =============================================

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Faculty added successfully."
            );


        } catch (Exception e) {

            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Faculty add failed: "
                    + e.getMessage()
            );
        }


        return "redirect:/faculty";
    }


    // =====================================================
    // VIEW FACULTY
    // =====================================================

    @GetMapping("/faculty/view/{id}")
    public String viewFaculty(
            @PathVariable Long id,
            Model model) {

        String url =
                "http://localhost:8082/api/faculty/"
                + id;

        FacultyResponse faculty =
                restTemplate.getForObject(
                        url,
                        FacultyResponse.class
                );

        model.addAttribute(
                "faculty",
                faculty
        );

        return "faculty/view";
    }


    // =====================================================
    // EDIT FACULTY PAGE
    // =====================================================

    @GetMapping("/faculty/edit/{id}")
    public String editFaculty(
            @PathVariable Long id,
            Model model) {

        String url =
                "http://localhost:8082/api/faculty/"
                + id;

        FacultyResponse faculty =
                restTemplate.getForObject(
                        url,
                        FacultyResponse.class
                );

        model.addAttribute(
                "faculty",
                faculty
        );

        model.addAttribute(
                "departments",
                departmentService.getAllDepartments()
        );

        return "faculty/edit";
    }


    // =====================================================
    // UPDATE FACULTY
    // =====================================================

    @PostMapping("/faculty/update")
    public String updateFaculty(

            @ModelAttribute FacultyResponse faculty,

            @RequestParam(
                    value = "photoFile",
                    required = false
            )
            MultipartFile photoFile,

            RedirectAttributes redirectAttributes) {

        try {

            String url =
                    "http://localhost:8082/api/faculty/"
                    + faculty.getId();


            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON
            );


            HttpEntity<FacultyResponse> request =
                    new HttpEntity<>(
                            faculty,
                            headers
                    );


            ResponseEntity<FacultyResponse>
                    response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PUT,
                            request,
                            FacultyResponse.class
                    );


            if (!response.getStatusCode()
                    .is2xxSuccessful()) {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "Faculty update failed."
                );

                return "redirect:/faculty";
            }


            // =============================================
            // PHOTO UPDATE ONLY IF NEW PHOTO SELECTED
            // =============================================

            if (photoFile != null &&
                !photoFile.isEmpty()) {

                String photoUrl =
                        "http://localhost:8082/api/faculty/"
                        + faculty.getId()
                        + "/photo";


                HttpHeaders photoHeaders =
                        new HttpHeaders();

                photoHeaders.setContentType(
                        MediaType.MULTIPART_FORM_DATA
                );


                MultiValueMap<String, Object> body =
                        new LinkedMultiValueMap<>();


                ByteArrayResource resource =
                        new ByteArrayResource(
                                photoFile.getBytes()
                        ) {

                            @Override
                            public String getFilename() {

                                return photoFile
                                        .getOriginalFilename();
                            }
                        };


                body.add(
                        "photo",
                        resource
                );


                HttpEntity<MultiValueMap<String, Object>>
                        photoRequest =
                        new HttpEntity<>(
                                body,
                                photoHeaders
                        );


                restTemplate.postForEntity(
                        photoUrl,
                        photoRequest,
                        FacultyResponse.class
                );
            }


            redirectAttributes.addFlashAttribute(
                    "success",
                    "Faculty updated successfully."
            );


        } catch (Exception e) {

            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Faculty update failed: "
                    + e.getMessage()
            );
        }


        return "redirect:/faculty";
    }


    // =====================================================
    // DELETE FACULTY
    // =====================================================

    @GetMapping("/faculty/delete/{id}")
    public String deleteFaculty(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {

            String url =
                    "http://localhost:8082/api/faculty/"
                    + id;

            restTemplate.delete(url);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Faculty deleted successfully."
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Faculty delete failed."
            );
        }

        return "redirect:/faculty";
    }
}