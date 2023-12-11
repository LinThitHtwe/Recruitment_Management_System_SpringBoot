function clearEmailForm() {
	$("#emailForm")[0].reset();
	$("#error-message").text('');
	resetErrorMessages();
}


function clearResetTokenForm() {
	$("#resetTokenForm")[0].reset();
	$("#error-message").text('');
	resetErrorMessages();
}

function clearResetPasswordForm() {
	$("#resetPasswordForm")[0].reset();
	$("#error-message").text('');
	resetErrorMessages();
}

function clearResetPasswordFormWithoutId() {
	document.getElementById("password").value = "";
	document.getElementById("confirm-password").value = "";
	$("#error-message").text('');
	resetErrorMessages();
}

function validateEmailForm() {
	var email = document.getElementById("email");

	// Reset error messages
	resetErrorMessages();

	if (email.value === "" || /^\s+$/.test(email.value)) {
		displayErrorMessage(email, "Email can't be blank.");
		return false;
	}
	return true;
}

function validateResetTokenForm() {
	var resetToken = document.getElementById("resetToken");
	resetErrorMessages();
	if (resetToken.value === "" || /^\s+$/.test(resetToken.value)) {
		displayErrorMessage(resetToken, "Code can't be blank.");
		return false;
	}
	return true;
}

function validateResetPasswordForm() {
	var password = document.getElementById("password");
	var confirmPassword = document.getElementById("confirmPassword");

	resetErrorMessages();
	
	 if (password.value.includes(' ')) {
		 console.log("spcae");
		 displayErrorMessage(password, "Password can't contain space.");
		return false;
		 }

	if (password.value.length < 6) {
		displayErrorMessage(password, "Password must be at least 6 characters long.");
		return false;
	}

	if (password.value !== confirmPassword.value) {
		displayErrorMessage(confirmPassword, "Passwords do not match.");
		return false;
	}

	return true;


}

 setTimeout(function() {
        $(".error-message").fadeOut();
    }, 3000);

    setTimeout(function() {
        $(".ban-message").fadeOut();
    }, 3000);


function displayErrorMessage(inputField, message) {
	var errorDiv = document.createElement("div");
	errorDiv.className = "error-message";
	errorDiv.innerHTML = message;

	// Apply color to the error message
	errorDiv.style.color = "red";
	errorDiv.style.fontSize = "13px";

	// Append error message after the input field
	inputField.parentNode.insertBefore(errorDiv, inputField.nextSibling);

	// Add error class to input field
	inputField.classList.add("error");

	// Clear error message when input field is clicked
	inputField.addEventListener("click", function() {
		errorDiv.parentNode.removeChild(errorDiv);
		inputField.classList.remove("error");
	});
}

function resetErrorMessages() {
	var errorMessages = document.getElementsByClassName("error-message");
	var inputFields = document.getElementsByClassName("form-control");

	// Remove error messages
	while (errorMessages.length > 0) {
		errorMessages[0].parentNode.removeChild(errorMessages[0]);
	}

	// Remove error class from input fields
	for (var i = 0; i < inputFields.length; i++) {
		inputFields[i].classList.remove("error");
	}
}

function showForgotPasswordModal() {
	$('#emailValidateFailModal').modal('hide');
	$('#forgotPasswordModal').modal('show');
}

function getEmail() {
	if (!validateEmailForm()) {
		return;
	}
	// Send the AJAX request
	$.ajax({
		url: "/validate-email",
		type: "POST",
		data: { email: $("#emailForm input[name='email']").val() },
		success: function(response) {
			if (response[0] !== null) {
				document.getElementById('validatedEmail').value = response[0];
				$('#emailValidateSuccessPlaceholder').text(response[1]);
				$('#forgotPasswordModal').modal('hide');
				clearEmailForm();
				$('#resetEmailSendReqModal').modal('show');
			}
			else {
				$('#emailValidateFailPlaceholder').text(response[1]);
				$('#forgotPasswordModal').modal('hide');
				clearEmailForm();
				$('#emailValidateFailModal').modal('show');

			}

		},
		error: function(xhr, status, error) {
			// Handle errors if any
			console.error(error);
		},
	});
}

document.addEventListener("click", function(event) {
	if (event.target.classList.contains("btn-close") || event.target.classList.contains("btn-secondary")) {
		// Get all open modals
		var openModals = document.querySelectorAll(".modal.show");

		// Hide each open modal using the Bootstrap .modal('hide') method
		openModals.forEach(function(modal) {
			$(modal).modal('hide');
		});
	}
});

function sendResetMail() {
	clearResetTokenForm();
	$('#resetEmailSendReqModal').modal('hide');
	$('#mailLoadingModal').modal('show');

	$.ajax({
		url: "/send-reset-mail",
		type: "POST",
		data: { validatedEmail: $("#sendResetMailForm input[name='validatedEmail']").val() },
		success: function(response) {
			if (response == "success") {
				$('#mailLoadingModal').modal('hide');
				$('#enterResetTokenModal').modal('show');
			}
			else {
				setTimeout(function() {
					$('#mailLoadingModal').modal('hide');
					$('#resetMailSendErrorPlaceholder').text(response);
					$('#sendResetMailFailModal').modal('show');
				}, 1000);
			}

		},
		error: function(xhr, status, error) {
			// Handle errors if any
			console.error(error);
		},
	});

}

function resendResetMail() {
	clearResetTokenForm();
	$('#sendResetMailFailModal').modal('hide');
	$('#enterResetTokenModal').modal('hide');
	$('#mailLoadingModal').modal('show');

	$.ajax({
		url: "/resend-reset-mail",
		type: "POST",
		data: { validatedEmail: $("#sendResetMailForm input[name='validatedEmail']").val() },
		success: function(response) {
			if (response == "success") {
				$('#mailLoadingModal').modal('hide');
				$('#enterResetTokenModal').modal('show');
			}
			else {
				setTimeout(function() {
					$('#mailLoadingModal').modal('hide');
					$('#resetMailSendErrorPlaceholder').text(response);
					$('#sendResetMailFailModal').modal('show');
				}, 1000);
			}

		},
		error: function(xhr, status, error) {
			// Handle errors if any
			console.error(error);
		},
	});
}

function validateResetToken() {
	if (!validateResetTokenForm()) {
		return;
	}
	$.ajax({
		url: "/validate-reset-token",
		type: "POST",
		data: { resetToken: $("#resetTokenForm input[name='resetToken']").val() },
		success: function(response) {
			if (response > 0) {
				$('#reset-id').val(response);
				$('#enterResetTokenModal').modal('hide');
				$('#resetPasswordModal').modal('show');

			} else {
				$('#enterResetTokenModal').modal('hide');
				$('#resetTokenValidateFailPlaceholder').text("Sorry! Your reset code may have expired or you have already reset password with this code.");
				$('#resetTokenValidateFailModal').modal('show');
			}
		}
	});
}

function retryTokenValidation() {
	clearResetTokenForm()
	$('#resetTokenValidateFailModal').modal('hide');
	$('#enterResetTokenModal').modal('show');
}

function resetPassword() {
	if (!validateResetPasswordForm()) {
		return;
	}
	$.ajax({
		url: "/reset-password",
		type: "POST",
		data: $("#resetPasswordForm").serialize(),
		success: function(response) {
			if (response == true) {
				setTimeout(function() {
					$('#resetPasswordModal').modal('hide');
					$("#resetPasswordSuccessModal").modal('show');
				}, 300);
				clearResetPasswordForm();
			} else {
				setTimeout(function() {
					$("#resetPasswordErrorModal").modal('show');
				}, 300);
			}
		},
		error: function(xhr, status, error) {
			// Handle errors if any
			console.error(error);
		},
	});
}

function retryResetingPassword() {
	clearResetPasswordFormWithoutId();
	$("#resetPasswordErrorModal").modal('hide');
	$('#resetPasswordModal').modal('show');
}