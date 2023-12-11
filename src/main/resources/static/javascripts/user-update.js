$(document).ready(function() {
	// Fetch user data from the server
	$.ajax({
		url: '/profile/userDetail',
		type: 'GET',
		dataType: 'json',
		success: function(data) {
			// Populate the form with user data
			$('#username').val(data.name);
			$('#email').val(data.email);
		},
		error: function(xhr, status, error) {
			console.error('Error fetching user data:', error);
		}
	});

	// Disable the update button on page load
	$('#updateButton').prop('disabled', true);

	// Function to validate the new password length and password match
	function validateAndUpdateButton() {
		var newPassword = $('#newPassword').val();
		var confirmPassword = $('#confirmPassword').val();
		var isValid = true;

		// Check if new password and confirm password match only after submitting
		if ($('#updateButton').data('submitted') && newPassword !== confirmPassword) {
			showError('New password and confirm password do not match.');
			isValid = false;
		}

		// Remove spaces from the new password
		newPassword = newPassword.replace(/\s/g, '');

		// Truncate new password to 20 characters
		if (newPassword.length > 20) {
			newPassword = newPassword.substring(0, 20);
		}

		// Update the input field after modifications
		$('#newPassword').val(newPassword);

		// Check if new password is at least 6 characters long only after submitting
		if ($('#updateButton').data('submitted') && newPassword.length < 6) {
			showError('New password should be at least 6 characters long.');
			isValid = false;
		}

		// Enable the update button if validation is successful
		if (isValid) {
			$('#updateButton').prop('disabled', false);
		} else {
			$('#updateButton').prop('disabled', true);
		}

		return isValid;
	}

	// Add event listener to the "New Password" input for validation
	$('#newPassword').on('input', function() {
		validateAndUpdateButton();
	});

	// Add event listener to the "Confirm Password" input for validation
	$('#confirmPassword').on('input', function() {
		// Mark the "Update" button as not submitted, so it won't trigger validation
		$('#updateButton').data('submitted', false);
		// Enable the "Update" button since the user can modify the fields again
		$('#updateButton').prop('disabled', false);
	});

	// Add event listener to the "Update" button
	$('#updateButton').click(function(event) {
		event.preventDefault(); // Prevent the default form submission behavior

		// Mark the button as submitted
		$(this).data('submitted', true);

		// Validate the password inputs
		var isValid = validateAndUpdateButton();

		// If validation fails, do not proceed with the AJAX request
		if (!isValid) {
			return;
		}

		// Send the data to the server using AJAX
		$.ajax({
			url: '/profile/update',
			type: 'POST',
			data: { oldPassword: $('#oldPassword').val(), password: $('#newPassword').val() },
			success: function(response) {
				// Show success message
				showSuccessMessage();
				// Optionally, you can clear the password fields after a successful update
				$('#oldPassword').val('');
				$('#newPassword').val('');
				$('#confirmPassword').val('');
			},
			error: function(xhr, status, error) {
				if (xhr.status === 400) {
					showError(xhr.responseText, true); // Show error message returned from the server
				} else {
					showError('An error occurred while processing your request.', true);
				}
			}
		});
	});

	// Function to show error messages for 3 seconds and then hide them
	function showError(message, isError) {
		var errorContainer = $('#errorContainer');
		var errorMessage = $('#errorMessage');

		if (isError) {
			errorContainer.addClass('error');
		} else {
			errorContainer.removeClass('error');
		}

		errorMessage.text(message);
		errorContainer.show();

		setTimeout(function() {
			errorContainer.hide();
		}, 3000);
	}

	// Function to show the success message
	function showSuccessMessage() {
		var successContainer = $('#successMessageContainer');
		var successMessage = $('#successMessage');

		successMessage.text('Password updated successfully!');
		successContainer.show();

		setTimeout(function() {
			successContainer.hide();
		}, 3000);
	}

	document.getElementById("backButton").addEventListener("click", function() {
		history.back();
	});
});
