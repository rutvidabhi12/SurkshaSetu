document.addEventListener("DOMContentLoaded", function () {

    const searchInput = document.getElementById("departmentSearch");
    const tableBody = document.getElementById("departmentTableBody");

    if (!searchInput || !tableBody) {
        console.log("Department search elements not found");
        return;
    }

    searchInput.addEventListener("keyup", function () {

        const searchValue = searchInput.value.toLowerCase().trim();

        const rows = tableBody.querySelectorAll("tr");

        rows.forEach(function (row) {

            const cells = row.querySelectorAll("td");

            // No Department Found row
            if (cells.length === 1) {
                return;
            }

            const departmentName = cells[1]
                ? cells[1].textContent.toLowerCase()
                : "";

            const departmentCode = cells[2]
                ? cells[2].textContent.toLowerCase()
                : "";

            const status = cells[3]
                ? cells[3].textContent.toLowerCase()
                : "";

            const searchText =
                departmentName + " " +
                departmentCode + " " +
                status;

            if (searchText.includes(searchValue)) {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }
        });
    });

});