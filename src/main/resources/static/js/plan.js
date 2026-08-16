document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".price").forEach((element) => {
        const value = Number(element.textContent);

        if (!Number.isNaN(value)) {
            element.textContent = value.toLocaleString("en-US");
        }
    });
});
