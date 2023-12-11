$(document).ready(function() {
	var confirmed = false;

	var currentRole = $("#currentUserRole").text();
	function loadUsers(page, rowCount) {

		$('#userTable').DataTable().ajax.reload();
	}

	$('#filterSelect, #roleFilterSelect, #departmentSort').on('change', function() {
		var selectedRole = $('#roleFilterSelect').val();
		var selectedStatus = $('#filterSelect').val();
		var selectedDepartment = $('#departmentSort').val();
		filterData(selectedRole, selectedStatus, selectedDepartment);
	});

	function filterData(role, status, department) {
		var table = $('#userTable').DataTable();
		var roleFilter = role === 'All' ? '' : role;
		var statusFilter = status === 'all' ? '' : status;
		var departmentFilter = department === 'All' ? '' : department;

		table.column(2).search(roleFilter);
		table.column(4).search(statusFilter);
		table.column(3).search(departmentFilter);

		table.draw();
	}

	function loadDepartmentsForSort(targetSelect, targetLabel) {
		$.ajax({
			url: "/admin/departments-for-user",
			type: "GET",
			success: function(response) {
				var departmentSelect = $(targetSelect);
				var departmentSelectLabel = $(targetLabel);

				// Clear any existing options
				departmentSelect.empty();

				// Add options for each department
				departmentSelect.append('<option value="All">All</option>');
				response.forEach(function(department) {
					var option = $("<option>")
						.val(department.name) // Use department name as the value
						.text(department.name);
					departmentSelect.append(option);
				});

				// Perform filtering based on the selected role, status, and department
				var selectedRole = $('#roleFilterSelect').val();
				var selectedStatus = $('#filterSelect').val();
				var selectedDepartment = $('#departmentSort').val();
				filterData(selectedRole, selectedStatus, selectedDepartment);
			},
			error: function(xhr, status, error) {
				console.error(error);
			}
		});
	}

	loadDepartmentsForSort("#departmentSort", "#departmentSortLabel");


	var table = $('#userTable').DataTable({
		processing: true,
		serverSide: true,
		ajax: {
			url: '/admin/users-data',
			type: 'GET',
		},
		columns: [
			{ data: 'name', name: 'Name' },
			{ data: 'email', name: 'Email' },
			{ data: 'role', name: 'Role' },
			{
				data: 'department.name',
				name: 'Department',
				render: function(data, type, row) {
					return data ? data : '<div class="dash">-</div>';;
				}
			},
			{
				data: "status",
				render: function(data, type, row) {
					var statusText = data ? "Inactive" : "Active";
					var statusClass = data ? "status-decline" : "status-considering";

					return '<div class="status-badge ' + statusClass + '">' + statusText + '</div>';
				},
				sortable: false
				
			},

			{
				data: "status",
				orderable: false,
				searchable: false,
				render: function(data, type, row) {
					var currentUserRole;

					$.ajax({
						url: '/hr/current-user-role',
						type: 'GET',
						async: false,
						success: function(role) {
							currentUserRole = role;
						},
						error: function() {
							currentUserRole = "";
						}
					});
					
					
					//console.log("row data--" + row.id + "-----=-" + row.email + "-----" + row.status);
					var statusText = data ? "Activate" : "Deactivate";
					var btnClass = data ? "active-btn text-white" : "inactive-btn text-white";

					if (currentUserRole === "Admin") {
						if (row.role === "Junior-HR" || row.role === "Senior-HR" || row.role === "Interviewer" || row.role === "Department-head") {
							return '<div class="btn-group" role="group">' +
								'<button type="button" class="btn ' + btnClass + ' statusButton" data-user-id="' + row.id + '" data-user-status="' + status + '" id="statusButton">' + statusText + '</button>' + '</div>';
						} else if (row.role === "Admin"|| row.role=="Default-Admin") {
							return '<div class="dash">-</div>';
						}
					} else if (currentUserRole === "Default-Admin") {
						if (row.role !== "Default-Admin") {

							return '<div class="btn-group" role="group">' +
								'<button type="button" class="btn ' + btnClass + ' statusButton" data-user-id="' + row.id + '" data-user-status="' + status + '" id="statusButton">' + statusText + '</button>' +
								'</div>';
						} else if (row.role === "Default-Admin") {
							return '<div class="dash">-</div>';
						}
					}
				},
				sortable: false
			},

			{
				data: "id",
				render: function(data, type, row) {
					var permissionButton = '';
					var permissionDownButton = '';
					var statusText = row.status ? "False" : "True";

					if (statusText === "True") {
						if (currentRole === "Admin") {
							if (row.role === "Junior-HR" || row.role === "Senior-HR" || row.role === "Interviewer" || row.role === "Department-head") {
								if (row.role === "Junior-HR") {
									permissionButton = '<button title="Give Permission" type="button" class="btn permission-btn" data-user-id="' + row.id + '" data-user-role="Senior-HR" id="permissionBtn"><i class="bi bi-arrow-up-circle-fill"></i></button>';
								} else if (row.role === "Senior-HR") {
									permissionButton = '<button title="Give Permission" type="button" class="btn permission-btn" data-user-id="' + row.id + '" data-user-role="Admin" id="permissionBtn"><i class="bi bi-arrow-up-circle-fill"></i></button>';
									permissionDownButton = '<button title="Permission Downgrade" type="button" class="btn permission-btn" data-user-id="' + row.id + '" data-user-role="Junior-HR" id="permissionDownBtn"><i class="bi bi-arrow-down-circle-fill"></i></button>';
								}

							return '<div class="btn-group" role="group">' + permissionButton + permissionDownButton + '</div>';
							} else if (row.role === "Admin"|| row.role==="Default-Admin") {
								return '<div class="dash">-</div>';
							}
						} else if (currentRole === "Default-Admin") {
							if (row.role !== "Default-Admin") {
								if (row.role === "Junior-HR") {
									permissionButton = '<button title="Give Permission" type="button" class="btn permission-btn" data-user-id="' + row.id + '" data-user-role="Senior-HR" id="permissionBtn"><i class="bi bi-arrow-up-circle-fill"></i></button>';
								} else if (row.role === "Senior-HR") {
									permissionButton = '<button title="Give Permission" type="button" class="btn permission-btn" data-user-id="' + row.id + '" data-user-role="Admin" id="permissionBtn"><i class="bi bi-arrow-up-circle-fill"></i></button>';
									permissionDownButton = '<button title="Permission Downgrade" type="button" class="btn permission-btn" data-user-id="' + row.id + '" data-user-role="Junior-HR" id="permissionDownBtn"><i class="bi bi-arrow-down-circle-fill"></i></button>';
								} else if (row.role === "Admin") {
									permissionDownButton = '<button title="Permission Downgrade" type="button" class="btn permission-btn" data-user-id="' + row.id + '" data-user-role="Senior-HR" id="permissionDownBtn"><i class="bi bi-arrow-down-circle-fill"></i></button>';
								}
								return '<div class="btn-group" role="group">' + permissionButton + permissionDownButton + '</div>';
							} else if (row.role === "Default-Admin") {
								return '<div class="dash">-</div>';
							}
						}
					}
					else {
						return '<div class="dash">-</div>';
					}
				},
				sortable: false
			}



		],
		order: [[0, 'asc']],
		paging: true,
		lengthMenu: [5, 10, 20],
		pageLength: 5,
	});

	$("#resetBtn").on("click", function() {
		$("#filterSelect").val("all");

		$("#roleFilterSelect").val("All");

		$("#departmentSort").val("All");


		table.clear().draw();
		table.columns().search('').draw();
		table.ajax.reload();
	});

	$('#userTable').on('click', '#permissionBtn', function() {
		var userId = $(this).data('user-id');
		var userRole = $(this).data('user-role');
		console.log(userId);
		console.log(userRole);

		// Show the modal with the user ID and role
		showModal(userId, userRole);
	});

	// Function to show the modal
	function showModal(userId, userRole) {
		var modalTitle = "Confirmation";
		var modalMessage = "Do you want to permit the user as " + userRole + "?";
		$('#confirmationModalLabel').text(modalTitle);
		$('#permissonMessage').text(modalMessage);

		// Set the data attributes for confirmButton
		$('#permitButton').data('user-id', userId);
		$('#permitButton').data('user-role', userRole);

		// Show the modal
		$('#permissionConfirmModal').modal('show');

		// Handle the click event on the Confirm button
		$('#permitButton').on('click', function() {
			// Get the updated user ID and role from the data attributes
			var userId = $(this).data('user-id');
			var userRole = $(this).data('user-role');

			// Send the data as JSON via POST request
			$.ajax({
				type: 'GET',
				url: '/admin/permit-user',
				data: { userId: userId, permitTo: userRole },
				success: function(response) {
					table.ajax.reload();
					$('#successAlert').show();

					setTimeout(function() {
						$('#successAlert').hide();
					}, 3000);


					console.log('Data sent successfully:', response);
				},
				error: function(error) {
					// Handle the error response if needed
					console.error('Error sending data:', error);
				}
			});

			$('#permissionConfirmModal').modal('hide');
		});
	}


	$('#userTable').on('click', '#permissionDownBtn', function() {
		var userId = $(this).data('user-id');
		var userRole = $(this).data('user-role');
		console.log(userId);
		console.log(userRole);

		// Show the modal with the user ID and role
		showDownModal(userId, userRole);
	});

	// Function to show the modal
	function showDownModal(userId, userRole) {
		console.log("show modal")
		var modalTitle = "Confirmation";
		var modalMessage = "Do you want to step down the user as " + userRole + "?";
		$('#confirmationDownModalLabel').text(modalTitle);
		$('#permissonDownMessage').text(modalMessage);

		// Set the data attributes for confirmButton
		$('#permitDownButton').data('user-id', userId);
		$('#permitDownButton').data('user-role', userRole);

		// Show the modal
		$('#permissionDownConfirmModal').modal('show');

		// Handle the click event on the Confirm button
		$('#permitDownButton').on('click', function() {
			// Get the updated user ID and role from the data attributes
			var userId = $(this).data('user-id');
			var userRole = $(this).data('user-role');

			// Send the data as JSON via POST request
			$.ajax({
				type: 'GET',
				url: '/admin/permit-user',
				data: { userId: userId, permitTo: userRole },
				success: function(response) {
					table.ajax.reload();
					$('#successAlert').show();

					setTimeout(function() {
						$('#successAlert').hide();
					}, 3000);


					console.log('Data sent successfully:', response);
				},
				error: function(error) {
					// Handle the error response if needed
					console.error('Error sending data:', error);
				}
			});

			$('#permissionDownConfirmModal').modal('hide');
		});
	}

	$('#userTable').on('click', '.statusButton', function() {
		var button = $(this);
		var userId = button.data('user-id');
		var userName = button.closest('tr').find('td:first-child').text();
		var statusToggle = button.hasClass('active-btn');
		console.log("USER ID AT EVENT ON CLICK--" + userId);
		console.log("User name at event on click--" + userName);
		console.log("status toggle at user event on click--" + statusToggle);
		var confirmationModal = $('#confirmationModal');
		var confirmationMessage = statusToggle ? 'Are you sure to enable the account for ' + userName + '?' : 'Are you sure to disable the account for ' + userName + '?';

		confirmationModal.find('.modal-body').text(confirmationMessage);

		var originalBtnClass = button.attr('class');

		confirmationModal.modal('show').one('click', '#confirmButton', function() {
			confirmed = true;

			var invertedStatusToggle = !statusToggle;
			console.log("intvertedStatus at on click" + invertedStatusToggle);

			updateUserStatus(userId, invertedStatusToggle);
			if (invertedStatusToggle) {
				button.removeClass('active-btn').addClass('inactive-btn').text('Active');
			} else {
				button.removeClass('inactive-btn').addClass('active-btn').text('Inactive');
			}

			confirmationModal.modal('hide');
		});

		confirmationModal.one('click', '#cancelButton', function() {

			confirmationModal.modal('hide');
		});

		confirmationModal.one('hidden.bs.modal', function() {
			if (!confirmed) {
				button.attr('class', originalBtnClass);
			}
			confirmed = false;
		});
	});

	//function to change user status
	function updateUserStatus(userId, status) {
		console.log("User Id in method ----" + userId);
		console.log("User status in method----" + status);
		$.ajax({
			url: '/admin/change-user-status',
			type: 'GET',
			contentType: 'application/json',
			data: {
				status: status,
				userId: userId
			},
			success: function(response) {
				table.ajax.reload();
				$('#successAlert').show();

				setTimeout(function() {
					$('#successAlert').hide();
				}, 3000);
			},
			error: function(xhr) {
				console.error('Error updating user status:', xhr);
			}
		});
	}


});

// Ready function
$(document).ready(function() {
	var submitButton = $("#addUserForm button[type='submit']");
	var successModal = $("#successModal");

	// Function for displaying error message at add position model box
	function displayErrorMessage(errorMessage, show, userInput) {
		if (errorMessage === 'duplicate') {
			errorMessage = userInput + ' already exists';
		}

		$("#error-msg").text(errorMessage);

		if (show) {
			$("#error-msg").show();
			submitButton.prop('disabled', true);
		} else {
			$("#error-msg").hide();
			submitButton.prop('disabled', false);
		}
	}

	// Checking duplicate email for adding user in model box
function checkDuplicateEmail(email) {
    $.ajax({
        type: 'POST',
        url: '/admin/duplicate-email',
        data: {
            email: email
        },
        success: function(response) {
            var emailErrorElement = $('#email-error');

            switch (response) {
                case 'Email already exists':
                    displayErrorMessage(emailErrorElement, email+' already exists');
                    break;
                case 'Valid email':
                   
                    break;
                case 'Invalid email format':
                    displayErrorMessage(emailErrorElement, 'Invalid email format');
                    break;
                default:
                    displayErrorMessage(emailErrorElement, '');
                    break;
            }
        },
        error: function(xhr) {
            var errorMessage = xhr.responseText;
            displayErrorMessage('#email-error', errorMessage);
        }
    });
}


	// Function for displaying error message
	function displayErrorMessage(elementId, errorMessage) {
		var element = $(elementId);
		element.text(errorMessage);

		// Toggle the visibility of the error message based on the presence of an error message
		if (errorMessage !== '') {
			element.show();
		} else {
			element.hide();
		}
	}

	// Function to retrieve departments and populate the select box
	function loadDepartments(targetSelect, targetLabel) {
		$.ajax({
			url: "/admin/departments-for-user",
			type: "GET",
			success: function(response) {
				var departmentSelect = $(targetSelect);
				var departmentSelectLabel = $(targetLabel);

				// Clear any existing options
				departmentSelect.empty();

				// Add options for each department
				departmentSelect.append('<option value="" disabled selected>Select Department</option>');
				response.forEach(function(department) {
					var option = $("<option>")
						.val(department.id)
						.text(department.name);
					departmentSelect.append(option);
				});

				// Show/hide the department select based on the selected role
				$("#role").on("change", handleRoleChange);

				// Check if both role and department are selected
				validateForm();
			},
			error: function(xhr, status, error) {
				console.error(error);
			}
		});
	}
	// Call the function to load departments for the add model on page load
	loadDepartments("#departmentSelect", "#departmentSelectLabel");

	// Call the function to load departments for the update model on page load
	loadDepartments("#departmentSelectUpdate", "#departmentSelectLabelUpdate");

	// Add model box function event
	// Function to add new user
	$('#addUserForm').submit(function(event) {
		event.preventDefault(); // Prevent default form submission

		var form = $(this);
		var url = form.attr('action');

		// Retrieve input field values
		var username = $('#username').val().trim();
		var email = $('#email').val().trim();
		var role = $('#role').val();
		var department = $('#departmentSelect').val();

		// Perform input validation
		if (username === '' || /^\s*$/.test(username) || username.length <= 2 || username.length > 50 || email === '' || /^\s*$/.test(email) || email.length > 40 || role === '' || role.length > 30 || (role === "Department-head" || role === "Interviewer") && department === "") {
			if (username.length <= 2) {
				console.log('fewer');
			}
			displayErrorMessage('#error-msg', 'Please enter valid data.');
			return;
		}

		// Disable the "Add" button while the form is being submitted
		submitButton.prop('disabled', true);

		// Prepare the user data for submission
		var userData = {
			name: username,
			email: email,
			role: role,
			department: department
		};

		$('#userModal').modal('hide');
		$('#mailLoadingModal').modal('show');

		$.ajax({
			type: 'POST',
			url: url,
			data: userData,
			success: function(response) {
				if (response == "success") {
					form.trigger('reset');
					$('#mailLoadingModal').modal('hide');
					showSuccessModal();
					$('#userTable').DataTable().ajax.reload();
				}
				else {
					setTimeout(function() {
						$('#mailLoadingModal').modal('hide');
						$('#mailSendErrorPlaceholder').text(response);
						$('#sendMailFailModal').modal('show');
					}, 1000);
				}
			},
			error: function(xhr) {
				var errorMessage = xhr.responseText;
				displayErrorMessage('#error-msg', errorMessage);
			},
			complete: function() {
				// Enable the "Add" button after the AJAX request is complete
				submitButton.prop('disabled', false);
			}
		});
	});

	function resendMail() {
		$('#sendMailFailModal').modal('hide');
		$('#mailLoadingModal').modal('show');

		var form = $('#addUserForm');
		var username = $('#username').val().trim();
		var email = $('#email').val().trim();
		var role = $('#role').val();
		var department = $('#departmentSelect').val();

		var userData = {
			name: username,
			email: email,
			role: role,
			department: department
		};

		$.ajax({
			url: "/admin/resend-password-mail",
			type: "POST",
			data: userData,
			success: function(response) {
				console.log('response' + response);
				if (response == "success") {
					form.trigger('reset');
					$('#mailLoadingModal').modal('hide');
					showSuccessModal();
				}

				else {
					setTimeout(function() {
						$('#mailLoadingModal').modal('hide');
						$('#mailSendErrorPlaceholder').text(response);
						$('#sendMailFailModal').modal('show');
					}, 1000);
				}

			},
			error: function(xhr, status, error) {
				// Handle errors if any
				console.error(error);
			},
			complete: function() {
				// Enable the "Add" button after the AJAX request is complete
				submitButton.prop('disabled', false);
			}
		});
	}

	$("#sendMailFailModal").on("click", "#resendMailBtn", function() {
		resendMail();
	});
var debounceTimer;
var emailInput = $('#email');
var emailError = $('#email-error');
$('#email').on('blur', function(event) {
    var email = $(this).val().trim();

    clearTimeout(debounceTimer);

    debounceTimer = setTimeout(function() {
        if (email !== '') {
            checkDuplicateEmail(email);
        } else {
            displayErrorMessage('#email-error', '');
        }
    }, 01);
});


emailInput.on('focus', function() {
    // Clear the error message when the input field gains focus
    displayErrorMessage(emailError, '');
});

// Listen for input changes to reset the debounce timer
emailInput.on('input', function() {
    clearTimeout(debounceTimer);
});


	// Function to show success modal
	function showSuccessModal() {
		$("#userModal").modal("hide");
		successModal.modal("show");
	}

	$("[id^=btnOK]").on("click", function(event) {
		event.preventDefault(); // Prevent the default behavior (page reload)

		// Get all open modals with class "modal"
		var openModals = $(".modal.show");

		// Close all open modals
		openModals.modal("hide");
	});


	// Function to validate the form and enable/disable the "Add" button
	function validateForm() {
		var username = $('#username').val().trim();
		var email = $('#email').val().trim();
		var role = $('#role').val();
		var department = $('#departmentSelect').val();
		// Determine if the selected role requires the department field
		var requiresDepartment = role === "Department-head" || role === "Interviewer";

		// Check if all the required fields are filled
		var allFieldsFilled = username !== '' && email !== '' && role !== '' && username.length <= 50 && email.length <= 40 && role.length <= 30 && !/^\s*$/.test(username) && !/^\s*$/.test(email);

		// Check if the department field is filled (if required) or the role doesn't require department
		var departmentFilled = !requiresDepartment || department !== '';

		// Enable the submit button if all conditions are met, otherwise disable it
		var submitButton = $('#addUserButton'); // Replace 'submitButton' with the ID or selector of your actual submit button
		if (allFieldsFilled && role !== '' && role !== null && departmentFilled) {
			if ((role === "Department-head" || role === "Interviewer") && department === null) {
				submitButton.prop('disabled', true);
				console.log("if inside");
			} else {
				submitButton.prop('disabled', false);
				console.log("if");
			}
		} else {
			submitButton.prop('disabled', true);
			console.log("else");
		}
	}

	// Event handler for role change
	function handleRoleChange(targetSelect, targetLabel) {
		var selectedRole = $(this).val();
		var departmentSelect = $(targetSelect);
		var departmentSelectLabel = $(targetLabel);

		if (selectedRole === "Department-head" || selectedRole === "Interviewer") {
			departmentSelect.show();
			departmentSelectLabel.show();
		} else {
			departmentSelect.hide();
			departmentSelectLabel.hide();
		}

		validateForm();
	}

	// Event handler for role change - Add Model
	$("#role").on("change", function(event) {
		var selectedRole = $(this).val();
		var departmentSelect = $('#departmentSelect');

		// Check if the role has changed to a value that doesn't require a department
		if (selectedRole !== "Department-head" && selectedRole !== "Interviewer") {
			// Set the department select value to null
			departmentSelect.val(null);
		}

		handleRoleChange.call(this, "#departmentSelect", "#departmentSelectLabel", selectedRole);
	});

	// Event handler for role change - Update Model
	$("#roleUpdate").on("change", function(event) {
		var selectedRole = $(this).val();
		handleRoleChange.call(this, "#departmentSelectUpdate", "#departmentSelectLabelUpdate", selectedRole);
	});

	// Bind input event handlers to other form fields (if necessary) to trigger validation on change
	$('#username, #email, #departmentSelect').on('input', function() {
		validateForm();
	});

	$("[id^=btnOK]").on("click", function(event) {

		$('#userTable').DataTable().ajax.reload();
	});

});
