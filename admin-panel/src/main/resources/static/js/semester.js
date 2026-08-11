document.addEventListener("DOMContentLoaded", function () {

    const searchInput = document.getElementById("semesterSearch");
    const tableBody = document.getElementById("semesterTableBody");

    if (!searchInput || !tableBody) {
        return;
    }

    searchInput.addEventListener("keyup", function () {

        const searchValue = this.value.toLowerCase().trim();

        const rows = tableBody.querySelectorAll("tr");

        rows.forEach(function (row) {

            const rowText = row.textContent.toLowerCase();

            row.style.display =
                rowText.includes(searchValue) ? "" : "none";

        });

    });

});