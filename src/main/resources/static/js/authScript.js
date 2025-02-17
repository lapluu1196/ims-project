$(document).ready(function () {
    const togglePassword = $('#togglePassword');
    const password = $('#password');
    const toggleConfirmPassword = $('#toggleConfirmPassword');
    const confirmPassword = $('#confirmPassword');
    const passwordError = $("#passwordError");
    const confirmPasswordError = $("#confirmPasswordError");

    togglePassword.on('click', function () {
        const type = password.attr('type') === 'password' ? 'text' : 'password';
        password.attr('type', type);
        this.classList.toggle('bi-eye');
    });

    toggleConfirmPassword.on('click', function () {
        const type = confirmPassword.attr('type') === 'password' ? 'text' : 'password';
        confirmPassword.attr('type', type);
        this.classList.toggle('bi-eye');
    });

    $('#forgotPasswordForm').on('submit', function (event) {
        event.preventDefault();

        var email = $('#email').val();

        $('#error-message').text('');
        $('#success-message').text('');

        $.ajax({
            url: '/api/auth/password/forgot',
            type: 'POST',
            data: {
                email: email
            },
            success: function (response) {
                $('#success-message').text("We've sent an email with the link to reset your password.");
            },
            error: function (xhr, status, error) {
                $('#error-message').text("The Email address doesn't exist. Please try again.");
            }
        });
    });

    $('#resetPasswordForm > form').on('submit', function (event) {
        if (!validateInputs()) {
            event.preventDefault();
        }
    });

    password.on("change", function () {
        validateInputs();
    });

    confirmPassword.on("change", function () {
        validateInputs();
    });


    function validateInputs() {
        const passwordValue = password.val();
        const confirmPasswordValue = confirmPassword.val();
        const passwordCriteria = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{7,}$/;
        let isValid = true;

        if (!passwordCriteria.test(passwordValue)) {
            passwordError.text("Use at least one letter, one number and seven characters");
            isValid = false;
        } else {
            passwordError.text("");
        }

        if (passwordValue !== confirmPasswordValue) {
            confirmPasswordError.text("Passwords do not match. Please try again.");
            isValid = false;
        } else {
            confirmPasswordError.text("");
        }

        return isValid;
    }

})