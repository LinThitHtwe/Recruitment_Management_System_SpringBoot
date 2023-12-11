$(document).ready(function() {
	var table = $('#departmentTable').DataTable({
		processing: true,
		serverSide: true,
		ajax: {
			url: '/admin/department-data',
			type: 'GET',
		},
		columns: [
			{ data: 'name', name: 'Department Name' },
			{ data: 'address', name: 'Address' },
			{ data: 'createdDateTime', name: 'Created Date/Time' },
			{
				data: 'updatedDateTime', name: 'Updated Date/Time',
				render: function(data) {
					return data !== null ? data : '-';
				}
			},

			{
				data: "id",
				render: function(data, type, row) {
					var infoButton = `<button title="Department's Info" type="button" class="btn btn-info info-btn" data-id="${row.id}" id="infoButton"><i class="ti ti-info"></i></button>`;
					var editButton = `<button title="Edit Department" type="button" class="btn btn-primary edit-btn" data-id="${row.id}"><i class="ti ti-pencil"></i></button>`;
					return `<div class="btn-group">${infoButton}${editButton}</div>`;
				},
				sortable: false
			},

		],
		order: [[0, 'asc']],
		paging: true,
		lengthMenu: [5, 10, 20], // Number of records per page
		pageLength: 5, // Default number of records per page
	});

	$(document).ready(function() {
		var form = $("#departmentAddForm");
		var nameInput = $("#add-name");
		var addressInput = $("#add-address");


		//make empty errors disappear when user inputs sth    
		nameInput.on('keyup', function(event) {
			if ($(this).val().trim() !== "") {
				displayEmptyError("add-name", "", false);
			}
		});

		addressInput.on('keyup', function(event) {
			if ($(this).val().trim() !== "") {
				displayEmptyError("add-address", "", false);
			}
		});

		//check if name and address inputs are empty on submit
		form.submit(function(event) {
			event.preventDefault();
			if (nameInput.val().trim() === "" || /^\s*$/.test(nameInput.val().trim()) || nameInput.val().trim().length <= 2 || addressInput.val().trim() === "" || /^\s*$/.test(addressInput.val().trim()) || addressInput.val().trim().length <= 2) {
				if (nameInput.val().trim() === "" || /^\s*$/.test(nameInput.val().trim()) || nameInput.val().trim().length <= 2) {
					displayEmptyError("add-name", "Please enter valid department name.", true);
				}
				else {
					displayEmptyError("add-name", "", false);
				}

				if (addressInput.val().trim() === "" || addressInput.val().trim() === "" || /^\s*$/.test(addressInput.val().trim()) || /^\s*$/.test(addressInput.val().trim()) || addressInput.val().trim().length <= 2) {
					displayEmptyError("add-address", "Please enter valid department address.", true);
				}
				else {
					displayEmptyError("add-address", "", false);
				}
				return;
			}


			//ajax to invoke add-department controller method
			$.ajax({
				type: "POST",
				url: "/admin/add-department",
				data: form.serialize(),
				success: function(response) {
					form.trigger("reset");
					$("#departmentAddModal").modal("hide");
					table.ajax.reload();

					//	$("#addSuccessModal").modal("show");
					$('#successAlert').show();

					// Hide the alert after 3 seconds (3000 milliseconds)
					setTimeout(function() {
						$('#successAlert').hide();
					}, 3000);

					// Refresh the table data using AJAX

				},
				error: function(xhr) {
					var errorMessage = xhr.responseText;
					displayErrorMessage(errorMessage, true);
				}
			});
		});

		function displayEmptyError(inputId, errorMsg, show) {
			var submitButton = $("#departmentAddForm button[type='submit']");
			var errorElement = $("#" + inputId + "EmptyError");
			errorElement.text(errorMsg);

			if (show) {
				errorElement.show();
				submitButton.prop('disabled', true);
			}
			else {
				errorElement.hide();
				submitButton.prop('disabled', false);
			}
		}

	});

	$(document).ready(function() {
		// Click event for edit buttons
		$(document).on('click', 'button.edit-btn', function() {
			var departmentId = $(this).data('id');
			showDepartmentUpdateForm(departmentId);
		});

		// Function to show the department update form
		function showDepartmentUpdateForm(departmentId) {
			$.ajax({
				url: "/admin/department-detail",
				type: "GET",
				data: { id: departmentId },
				success: function(response) {
					// Populate form fields
					$('#id').val(response.id);
					$('#name').val(response.name);
					$('#address').val(response.address);

					$('#departmentUpdateModal').modal('show');
				},
				error: function(xhr, status, error) {
					console.error(error);
				}
			});
		}
	});

	// Clear the position modal on close
	$('#departmentTable').on('hidden.bs.modal', function() {
		$('#positionUpdateModalLabel').text('Position - Update');
		$('#positionUpdateName').val('');
		$('#error-msg-update').text('').hide();
		$('#updatePositionForm button[type="submit"]').prop('disabled', false); // Enable the submit button
	});




	$(document).ready(function() {
		// Click event for detail buttons
		$(document).on('click', 'button.info-btn', function() {
			var departmentId = $(this).data('id');
			showDepartmentDetail(departmentId);
		});

		// Function to show the department detail modal
		function showDepartmentDetail(departmentId) {
			$.ajax({
				url: "/admin/department-detail",
				type: "GET",
				data: { id: departmentId },
				success: function(response) {
					// Populate placeholders with department information
					$('#namePlaceholder').text(response.name);
					$('#addressPlaceholder').text(response.address);
					$('#createdUserPlaceholder').text(response.createdUsername);
					$('#createdDatetimePlaceholder').text(response.createdDateTime);
					$('#updatedUserPlaceholder').text(response.updatedUsername !== null ? response.updatedUsername : '-');
					$('#updatedDatetimePlaceholder').text(response.updatedDateTime !== null ? response.updatedDateTime : '-');
					// Show the department detail modal
					$('#departmentDetailModal').modal('show');
				},
				error: function(xhr, status, error) {
					console.error(error);
				}
			});
		}

		$(document).on('hidden.bs.modal', '.modal.fade', function(e) {

			table.ajax.reload();
		});


		//function to check empty valids and update department for update
		$(document).ready(function() {
			var form = $("#departmentUpdateForm");
			var nameInput = $("#name");
			var addressInput = $("#address");


			//make empty errors disappear when user inputs sth for update  
			nameInput.on('keyup', function(event) {
				if ($(this).val().trim() !== "") {
					displayUpdateEmptyError("name", "", false);
				}
			});

			addressInput.on('keyup', function(event) {
				if ($(this).val().trim() !== "") {
					displayUpdateEmptyError("address", "", false);
				}
			});

			//check if name and address inputs are empty on submit for update
			form.submit(function(event) {
				event.preventDefault();
				if (nameInput.val().trim() === "" || /^\s*$/.test(nameInput.val().trim()) || nameInput.val().trim().length <= 2 || addressInput.val().trim() === "" || /^\s*$/.test(addressInput.val().trim()) || addressInput.val().trim().length <= 2) {
					if (nameInput.val().trim() === "" || /^\s*$/.test(nameInput.val().trim()) || nameInput.val().trim().length <= 2) {
						displayUpdateEmptyError("name", "Please enter valid department name.", true);
					}
					else {
						displayUpdateEmptyError("name", "", false);
					}

					if (addressInput.val().trim() === "" || addressInput.val().trim() === "" || /^\s*$/.test(addressInput.val().trim()) || /^\s*$/.test(addressInput.val().trim()) || addressInput.val().trim().length <= 2) {
						displayUpdateEmptyError("address", "Please enter valid department address.", true);
					}
					else {
						displayUpdateEmptyError("address", "", false);
					}
					return;
				}

				//ajax to invoke update-department controller method
				$.ajax({
					type: "POST",
					url: "/admin/update-department",
					data: form.serialize(),
					success: function(response) {

						$("#departmentUpdateModal").modal("hide");
						table.ajax.reload();
						$('#successAlert').show();
						setTimeout(function() {
							$('#successAlert').hide();
						}, 3000);

						// Refresh the table data using AJAX


					},
					error: function(xhr) {
						var errorMessage = xhr.responseText;
						displayErrorMessage(errorMessage, true);
					}
				});
			});


			//to display error messages for empty inputs for update
			function displayUpdateEmptyError(inputId, errorMsg, show) {
				var updateSubmitButton = $("#departmentUpdateForm button[type='submit']");
				var errorElement = $("#" + inputId + "EmptyError");
				errorElement.text(errorMsg);

				if (show) {
					errorElement.show();
					updateSubmitButton.prop('disabled', true);
				}
				else {
					errorElement.hide();
					updateSubmitButton.prop('disabled', false);
				}
			}

		});
	});

	$(document).ready(function() {
		var debounceTimeout;
		var submitButton = $("#departmentAddForm button[type='submit']");

		$('#add-name').on('input', function(event) {
			var departmentName = $(this).val();
			clearTimeout(debounceTimeout);

			debounceTimeout = setTimeout(function() {
				checkDuplicateDepartment(departmentName);
			}, 500);
		});

		//check duplcation with ajax
		function checkDuplicateDepartment(departmentName) {
			if (departmentName.trim() === "") {
				displayErrorMessage('', false);
				return;
			}

			$.ajax({
				type: 'POST',
				url: '/admin/duplicate-department-add',
				data: { name: departmentName },
				success: function(response) {
					if (response) {
						displayErrorMessage(departmentName + ' already exists', true);
					} else {
						displayErrorMessage('', false);
					}
				},
				error: function(xhr) {
					var errorMessage = xhr.responseText;
					displayErrorMessage(errorMessage, true);
				}
			});
		}

		//display error msg if duplicate
		function displayErrorMessage(errorMessage, show) {
			$("#error-msg").text(errorMessage);

			if (show) {
				$("#error-msg").show();
				submitButton.prop('disabled', true);
			} else {
				$("#error-msg").hide();
				submitButton.prop('disabled', false);
			}
		}
	});

	function resetAddForm() {
		$("#departmentAddForm")[0].reset();
		$("#error-msg").text('');
		$("#add-nameEmptyError").text('');
		$('#add-addressEmptyError').text('');
	}

	//check duplicate department for update
	$(document).ready(function() {
		var debounceTimeout;
		var updateSubmitButton = $("#departmentUpdateForm button[type='submit']");

		$('#name').on('input', function(event) {
			var departmentName = $(this).val();
			var departmentId = $('#id').val();
			clearTimeout(debounceTimeout);

			debounceTimeout = setTimeout(function() {
				checkDuplicateDepartmentUpdate(departmentName, departmentId);
			}, 500);
		});

		//check duplcation with ajax for update
		function checkDuplicateDepartmentUpdate(departmentName, departmentId) {
			if (departmentName.trim() === "") {
				displayErrorMessageUpdate('', false);
				return;
			}

			$.ajax({
				type: 'POST',
				url: '/admin/duplicate-department-update',
				data: { name: departmentName, id: departmentId },
				success: function(response) {
					if (response) {
						displayErrorMessageUpdate(departmentName + ' already exists', true);
					} else {
						displayErrorMessageUpdate('', false);
					}
				},
				error: function(xhr) {
					var errorMessage = xhr.responseText;
					displayErrorMessage(errorMessage, true);
				}
			});
		}

		$("[id^=btnOK]").on("click", function(event) {

			// Get all open modals with class "modal"
			var openModals = $(".modal.show");

			// Close all open modals
			openModals.modal("hide");
			$("#departmentTable").load(window.location.href + " #departmentTable");
		});

		function displayErrorMessageUpdate(errorMessage, show) {
			$("#error-msg-update").text(errorMessage);

			if (show) {
				$("#error-msg-update").show();
				updateSubmitButton.prop('disabled', true);
			} else {
				$("#error-msg-update").hide();
				updateSubmitButton.prop('disabled', false);
			}
		}
	});



	//reset update form when close the modal
	function resetUpdateForm() {
		$("#departmentUpdateForm")[0].reset();
		$("#error-msg-update").text('');
		$("#nameEmptyError").text('');
		$('#addressEmptyError').text('');
	}

});