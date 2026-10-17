
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".complaint-update-form").forEach(form => {
        const status = form.querySelector(".status-select");
        const resolutionNote = form.querySelector(".resolution-note");
        const rejectionReason = form.querySelector(".rejection-reason");

        // Display validation errors inside the form
        const errorBox = document.createElement("div");
        errorBox.className = "alert alert-danger py-2";
        errorBox.setAttribute("role", "alert");
        errorBox.style.display = "none";
        form.prepend(errorBox);

        // Use our own validation message instead of browser tooltip
        form.noValidate = true;

        form.addEventListener("submit", function (event) {
            let message = "";

            if (status.value === "RESOLVED" &&
                !resolutionNote.value.trim()) {
                message = "Resolution note is required to resolve a complaint.";
            } else if (status.value === "REJECTED" &&
                !rejectionReason.value.trim()) {
                message = "Rejection reason is required to reject a complaint.";
            }

            if (message) {
                event.preventDefault();
                errorBox.textContent = message;
                errorBox.style.display = "block";
            } else {
                errorBox.textContent = "";
                errorBox.style.display = "none";
            }
        });

        [status, resolutionNote, rejectionReason].forEach(field => {
            field.addEventListener("input", () => {
                errorBox.style.display = "none";
            });

            field.addEventListener("change", () => {
                errorBox.style.display = "none";
            });
        });
    });
});
