document.addEventListener("DOMContentLoaded", function () {

    const searchInput = document.getElementById("departmentSearch");
    const tableBody = document.getElementById("departmentTableBody");

    if (!searchInput || !tableBody) {
        console.log("Department search elements not found");
        return;
    }

    searchInput.addEventListener("input", function () {

        const searchValue = this.value.toLowerCase().trim();

        const rows = tableBody.querySelectorAll(
            "tr[data-department-row]"
        );

        rows.forEach(function (row) {

            const rowText = row.textContent.toLowerCase();

            if (rowText.includes(searchValue)) {

                row.style.display = "";

            } else {

                row.style.display = "none";

            }

        });

    });

});