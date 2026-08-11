document.addEventListener("DOMContentLoaded", function () {

    const searchInput = document.getElementById("studentSearch");

    if (!searchInput) {
        return;
    }

    searchInput.addEventListener("keyup", function () {

        const searchValue = this.value.toLowerCase().trim();

        const rows = document.querySelectorAll(
            "#studentTableBody tr"
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