document.addEventListener('DOMContentLoaded', () => {
    const userForm = document.getElementById('userForm');

    if (userForm) {
        userForm.addEventListener('submit', (event) => {
            const mobileInput = document.getElementById('mobileNumber');
            const mobileRegex = /^09\d{9}$/;

            if (!mobileRegex.test(mobileInput.value.trim())) {
                alert('Please enter a valid Iranian mobile number (e.g. 09123456789).');
                mobileInput.focus();
                event.preventDefault();
            }
        });
    }
});