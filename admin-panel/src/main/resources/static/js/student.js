document.addEventListener("DOMContentLoaded", function () {

    /* ==================================================
       MAIN DEPARTMENT & COURSE
    ================================================== */

    const mainDepartment =
        document.getElementById("mainDepartment");

    const mainCourse =
        document.getElementById("mainCourse");

    const uploadExcelBtn =
        document.getElementById("uploadExcelBtn");

    const excelFile =
        document.getElementById("excelFile");

    const uploadExcelForm =
        document.getElementById("uploadExcelForm");

    const uploadDepartmentId =
        document.getElementById("uploadDepartmentId");

    const uploadCourseId =
        document.getElementById("uploadCourseId");


    /* ==================================================
       DEPARTMENT → COURSE
    ================================================== */

    if (mainDepartment && mainCourse) {

        const allCourses =
            Array.from(mainCourse.options).map(function (option) {
                return option.cloneNode(true);
            });


        mainDepartment.addEventListener(
            "change",
            function () {

                const departmentId =
                    this.value;


                mainCourse.innerHTML =
                    '<option value="">Select Course</option>';


                if (!departmentId) {
                    return;
                }


                allCourses.forEach(
                    function (course) {

                        if (!course.value) {
                            return;
                        }


                        const courseDepartment =
                            course.getAttribute(
                                "data-department"
                            );


                        if (
                            String(courseDepartment) ===
                            String(departmentId)
                        ) {

                            mainCourse.appendChild(
                                course.cloneNode(true)
                            );

                        }

                    }
                );

            }
        );

    }


    /* ==================================================
       UPLOAD EXCEL BUTTON
    ================================================== */

    if (uploadExcelBtn) {

        uploadExcelBtn.addEventListener(
            "click",
            function () {

                const department =
                    mainDepartment.value;


                const course =
                    mainCourse.value;


                /* Department check */

                if (!department) {

                    Swal.fire({

                        icon: "warning",

                        title: "Department Required",

                        text:
                            "Please select a department first."

                    });

                    return;
                }


                /* Course check */

                if (!course) {

                    Swal.fire({

                        icon: "warning",

                        title: "Course Required",

                        text:
                            "Please select a course first."

                    });

                    return;
                }


                /* Save selected IDs */

                uploadDepartmentId.value =
                    department;

                uploadCourseId.value =
                    course;


                /* Open Excel file picker */

                excelFile.click();

            }
        );

    }


    /* ==================================================
       EXCEL FILE SELECT
    ================================================== */

    if (excelFile) {

        excelFile.addEventListener(
            "change",
            function () {

                const file =
                    this.files[0];


                if (!file) {
                    return;
                }


                /* Set Department ID */

                uploadDepartmentId.value =
                    mainDepartment.value;


                /* Set Course ID */

                uploadCourseId.value =
                    mainCourse.value;


                Swal.fire({

                    title: "Excel Selected",

                    text:
                        file.name +
                        " selected successfully.",

                    icon: "success",

                    showCancelButton: true,

                    confirmButtonText: "Upload",

                    cancelButtonText: "Cancel"

                }).then(
                    function (result) {

                        if (result.isConfirmed) {

                            uploadDepartmentId.value =
                                mainDepartment.value;


                            uploadCourseId.value =
                                mainCourse.value;


                            uploadExcelForm.submit();

                        }

                    }
                );

            }
        );

    }


    /* ==================================================
       STUDENT SEARCH
    ================================================== */

    const searchInput =
        document.getElementById("studentSearch");

    const rows =
        Array.from(
            document.querySelectorAll(".student-row")
        );


    if (searchInput) {

        searchInput.addEventListener(
            "input",
            function () {

                applyFilters();

            }
        );

    }


    /* ==================================================
       FILTER VARIABLES
    ================================================== */

    const filterDepartment =
        document.getElementById("filterDepartment");

    const filterCourse =
        document.getElementById("filterCourse");

    const filterSemester =
        document.getElementById("filterSemester");


    /* ==================================================
       PAGINATION
    ================================================== */

    const rowsPerPage = 100;

    let currentPage = 1;

    let filteredRows = [...rows];


    /* ==================================================
       TOTAL STUDENTS
    ================================================== */

    const totalStudents =
        document.getElementById("totalStudents");


    if (totalStudents) {

        totalStudents.textContent =
            rows.length;

    }


    /* ==================================================
       FILTER COURSE BY DEPARTMENT
    ================================================== */

    function updateCourseOptions(
        departmentSelect,
        courseSelect
    ) {

        const departmentId =
            departmentSelect.value;


        Array.from(
            courseSelect.options
        ).forEach(
            function (option) {

                if (!option.value) {

                    option.style.display = "";

                    return;

                }


                const courseDepartment =
                    option.getAttribute(
                        "data-department"
                    );


                if (
                    !departmentId ||
                    courseDepartment === departmentId
                ) {

                    option.style.display = "";

                } else {

                    option.style.display = "none";

                }

            }
        );


        courseSelect.value = "";

    }


    /* ==================================================
       SEMESTER FILTER BY COURSE
    ================================================== */

    function updateSemesterOptions() {

        if (!filterCourse || !filterSemester) {
            return;
        }


        const courseId =
            filterCourse.value;


        Array.from(
            filterSemester.options
        ).forEach(
            function (option) {

                if (!option.value) {

                    option.style.display = "";

                    return;

                }


                const semesterCourse =
                    option.getAttribute(
                        "data-course"
                    );


                if (
                    !courseId ||
                    semesterCourse === courseId
                ) {

                    option.style.display = "";

                } else {

                    option.style.display = "none";

                }

            }
        );


        filterSemester.value = "";

    }


    /* ==================================================
       FILTER DEPARTMENT CHANGE
    ================================================== */

    if (filterDepartment && filterCourse) {

        filterDepartment.addEventListener(
            "change",
            function () {

                updateCourseOptions(
                    filterDepartment,
                    filterCourse
                );

                updateSemesterOptions();

            }
        );

    }


    /* ==================================================
       FILTER COURSE CHANGE
    ================================================== */

    if (filterCourse) {

        filterCourse.addEventListener(
            "change",
            function () {

                updateSemesterOptions();

            }
        );

    }


    /* ==================================================
       APPLY FILTER
    ================================================== */

    window.applyStudentFilter =
        function () {

            applyFilters();


            const modalElement =
                document.getElementById(
                    "studentFilterModal"
                );


            if (modalElement) {

                const modal =
                    bootstrap.Modal.getInstance(
                        modalElement
                    );


                if (modal) {
                    modal.hide();
                }

            }

        };


    /* ==================================================
       RESET FILTER
    ================================================== */

    window.resetStudentFilter =
        function () {

            if (filterDepartment) {
                filterDepartment.value = "";
            }

            if (filterCourse) {
                filterCourse.value = "";
            }

            if (filterSemester) {
                filterSemester.value = "";
            }

            if (searchInput) {
                searchInput.value = "";
            }


            if (filterCourse) {

                Array.from(
                    filterCourse.options
                ).forEach(
                    function (option) {

                        option.style.display = "";

                    }
                );

            }


            if (filterSemester) {

                Array.from(
                    filterSemester.options
                ).forEach(
                    function (option) {

                        option.style.display = "";

                    }
                );

            }


            applyFilters();


            const modalElement =
                document.getElementById(
                    "studentFilterModal"
                );


            if (modalElement) {

                const modal =
                    bootstrap.Modal.getInstance(
                        modalElement
                    );


                if (modal) {
                    modal.hide();
                }

            }

        };


    /* ==================================================
       FILTER LOGIC
    ================================================== */

    function applyFilters() {

        const departmentId =
            filterDepartment
                ? filterDepartment.value
                : "";


        const courseId =
            filterCourse
                ? filterCourse.value
                : "";


        const semesterId =
            filterSemester
                ? filterSemester.value
                : "";


        const searchText =
            searchInput
                ? searchInput.value
                    .trim()
                    .toLowerCase()
                : "";


        filteredRows =
            rows.filter(
                function (row) {

                    const rowDepartment =
                        row.getAttribute(
                            "data-department"
                        );


                    const rowCourse =
                        row.getAttribute(
                            "data-course"
                        );


                    const rowSemester =
                        row.getAttribute(
                            "data-semester"
                        );


                    const rowSearch =
                        (
                            row.getAttribute(
                                "data-search"
                            ) || ""
                        ).toLowerCase();


                    const departmentMatch =
                        !departmentId ||
                        rowDepartment === departmentId;


                    const courseMatch =
                        !courseId ||
                        rowCourse === courseId;


                    const semesterMatch =
                        !semesterId ||
                        rowSemester === semesterId;


                    const searchMatch =
                        !searchText ||
                        rowSearch.includes(searchText);


                    return (
                        departmentMatch &&
                        courseMatch &&
                        semesterMatch &&
                        searchMatch
                    );

                }
            );


        currentPage = 1;

        updateFilterStatus();

        showPage(currentPage);

    }


    /* ==================================================
       FILTER STATUS
    ================================================== */

    function updateFilterStatus() {

        const status =
            document.getElementById(
                "filterStatus"
            );


        if (!status) {
            return;
        }


        const activeFilters = [];


        if (
            filterDepartment &&
            filterDepartment.value
        ) {

            activeFilters.push("Department");

        }


        if (
            filterCourse &&
            filterCourse.value
        ) {

            activeFilters.push("Course");

        }


        if (
            filterSemester &&
            filterSemester.value
        ) {

            activeFilters.push("Semester");

        }


        if (activeFilters.length > 0) {

            status.textContent =
                activeFilters.length +
                " filter(s) applied";

        } else {

            status.textContent = "";

        }

    }


    /* ==================================================
       SHOW PAGE
    ================================================== */

    function showPage(page) {

        currentPage = page;


        const totalPages =
            Math.ceil(
                filteredRows.length /
                rowsPerPage
            );


        const start =
            (page - 1) *
            rowsPerPage;


        const end =
            Math.min(
                start + rowsPerPage,
                filteredRows.length
            );


        rows.forEach(
            function (row) {

                row.style.display = "none";

            }
        );


        for (
            let i = start;
            i < end;
            i++
        ) {

            filteredRows[i].style.display = "";

        }


        const noData =
            document.getElementById(
                "noStudentData"
            );


        if (noData) {

            noData.style.display =
                filteredRows.length === 0
                    ? ""
                    : "none";

        }


        const startRecord =
            document.getElementById(
                "startRecord"
            );


        const endRecord =
            document.getElementById(
                "endRecord"
            );


        const filteredRecords =
            document.getElementById(
                "filteredRecords"
            );


        if (startRecord) {

            startRecord.textContent =
                filteredRows.length === 0
                    ? 0
                    : start + 1;

        }


        if (endRecord) {

            endRecord.textContent =
                end;

        }


        if (filteredRecords) {

            filteredRecords.textContent =
                filteredRows.length;

        }


        createPagination(totalPages);

    }


    /* ==================================================
       PAGINATION
    ================================================== */

    function createPagination(totalPages) {

        const pagination =
            document.getElementById(
                "studentPagination"
            );


        if (!pagination) {
            return;
        }


        pagination.innerHTML = "";


        if (totalPages <= 1) {
            return;
        }


        /* Previous */

        const previous =
            document.createElement("li");


        previous.className =
            "page-item " +
            (
                currentPage === 1
                    ? "disabled"
                    : ""
            );


        previous.innerHTML =
            `
            <a class="page-link" href="#">
                Previous
            </a>
            `;


        previous
            .querySelector("a")
            .addEventListener(
                "click",
                function (event) {

                    event.preventDefault();


                    if (currentPage > 1) {

                        showPage(
                            currentPage - 1
                        );

                    }

                }
            );


        pagination.appendChild(previous);


        /* Page Numbers */

        for (
            let page = 1;
            page <= totalPages;
            page++
        ) {

            const pageItem =
                document.createElement("li");


            pageItem.className =
                "page-item " +
                (
                    page === currentPage
                        ? "active"
                        : ""
                );


            pageItem.innerHTML =
                `
                <a class="page-link" href="#">
                    ${page}
                </a>
                `;


            pageItem
                .querySelector("a")
                .addEventListener(
                    "click",
                    function (event) {

                        event.preventDefault();

                        showPage(page);

                    }
                );


            pagination.appendChild(pageItem);

        }


        /* Next */

        const next =
            document.createElement("li");


        next.className =
            "page-item " +
            (
                currentPage === totalPages
                    ? "disabled"
                    : ""
            );


        next.innerHTML =
            `
            <a class="page-link" href="#">
                Next
            </a>
            `;


        next
            .querySelector("a")
            .addEventListener(
                "click",
                function (event) {

                    event.preventDefault();


                    if (
                        currentPage < totalPages
                    ) {

                        showPage(
                            currentPage + 1
                        );

                    }

                }
            );


        pagination.appendChild(next);

    }


    /* ==================================================
       INITIAL PAGE
    ================================================== */

    showPage(1);

});


/* ==================================================
   DELETE STUDENT
================================================== */

function deleteStudent(id) {

    Swal.fire({

        title: "Delete Student?",

        text:
            "This student will be permanently deleted.",

        icon: "warning",

        showCancelButton: true,

        confirmButtonColor: "#dc3545",

        cancelButtonColor: "#6c757d",

        confirmButtonText: "Yes, Delete",

        cancelButtonText: "Cancel"

    }).then(
        function (result) {

            if (result.isConfirmed) {

                window.location.href =
                    "/students/delete/" + id;

            }

        }
    );

}

function openExcelUpload() {

    const department =
        document.getElementById("mainDepartment").value;

    const course =
        document.getElementById("mainCourse").value;


    if (!department) {

        Swal.fire({
            icon: "warning",
            title: "Department Required",
            text: "Please select a department first."
        });

        return;
    }


    if (!course) {

        Swal.fire({
            icon: "warning",
            title: "Course Required",
            text: "Please select a course first."
        });

        return;
    }


    // Set selected IDs
    document.getElementById("uploadDepartmentId").value =
        department;

    document.getElementById("uploadCourseId").value =
        course;


    // Open Excel file picker
    document.getElementById("excelFile").click();
}