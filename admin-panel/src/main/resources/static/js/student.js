document.addEventListener("DOMContentLoaded", function () {

    // =========================================
    // Department → Course Filter
    // =========================================

    const departmentSelect =
        document.getElementById("departmentId");

    const courseSelect =
        document.getElementById("courseId");

    if (departmentSelect && courseSelect) {

        const allCourses = Array.from(
            courseSelect.querySelectorAll(
                "option[data-department-id]"
            )
        ).map(function (option) {
            return option.cloneNode(true);
        });

        departmentSelect.addEventListener("change", function () {

            const departmentId = this.value;

            courseSelect.innerHTML =
                '<option value="">Select Course</option>';

            if (!departmentId) {
                return;
            }

            allCourses.forEach(function (course) {

                const courseDepartmentId =
                    course.getAttribute("data-department-id");

                if (
                    String(courseDepartmentId) ===
                    String(departmentId)
                ) {
                    courseSelect.appendChild(
                        course.cloneNode(true)
                    );
                }

            });

        });
    }


    // =========================================
    // Student Search
    // =========================================

    const searchInput =
        document.getElementById("studentSearch");

    const tableBody =
        document.getElementById("studentTableBody");

    if (!searchInput || !tableBody) {
        console.log("Search elements not found");
        return;
    }

    searchInput.addEventListener("input", function () {

        const searchValue =
            this.value.toLowerCase().trim();

        const rows =
            tableBody.querySelectorAll(
                "tr[data-student-row]"
            );

        rows.forEach(function (row) {

            const rowText =
                row.textContent.toLowerCase();

            if (rowText.includes(searchValue)) {

                row.style.display = "";

            } else {

                row.style.display = "none";

            }

        });

    });

});