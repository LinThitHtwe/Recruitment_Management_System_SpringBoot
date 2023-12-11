// Ready function
$(document).ready(function() {
	var debounceTimeout;
	var submitButton = $("#addPositionForm button[type='submit']");
	var successModal = $("#successModal");
	var selectedItemsPerPage = 5;

	//Function for displaying error message at add position model box
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

	//Function for displaying error message at update position model box
	function displayErrorMessageUpdate(errorMessage, show, userInput) {
		if (errorMessage === 'duplicate') {
			errorMessage = userInput + ' already exists';
		}

		$("#error-msg-update").text(errorMessage);

		if (show) {
			$("#error-msg-update").show();
			$("#updatePositionForm button[type='submit']").prop('disabled', true);
		} else {
			$("#error-msg-update").hide();
			$("#updatePositionForm button[type='submit']").prop('disabled', false);
		}
	}

	//checking duplicate position for adding position model box
	function checkDuplicatePosition(positionName) {
		$.ajax({

			type: 'POST',
			url: '/hr/duplicate-position',
			data: {
				name: positionName
			}

			,
			success: function(response) {
				if (response) {
					displayErrorMessage(positionName + ' already exists', true, positionName);
				} else {
					displayErrorMessage('', false);
				}
			}

			,
			error: function(xhr) {
				var errorMessage = xhr.responseText;
				displayErrorMessage(errorMessage, true);
			}
		});
	}

	//checking duplicate position for update position model box
	function checkDuplicatePositionUpdate(positionId, positionName) {
		$.ajax({

			type: 'POST',
			url: '/hr/duplicate-position-update',
			data: {
				id: positionId,
				name: positionName
			}

			,
			success: function(response) {
				if (response) {
					displayErrorMessageUpdate(positionName + ' already exists', true, positionName);
				} else {
					displayErrorMessageUpdate('', false);
				}
			}

			,
			error: function(xhr) {
				var errorMessage = xhr.responseText;
				displayErrorMessageUpdate(errorMessage, true);
			}
		});
	}

	// Add model box function event
	$('#name').on('input', function(event) {
		var positionName = $(this).val().trim();
		var addButton = $('#addPositionForm button[type="submit"]');

		// Disable the button if positionName is blank or if it's a duplicate
		addButton.prop('disabled', positionName === '' || /^\s*$/.test(positionName) ||  positionName.length <= 2 || positionName.length > 255 || checkDuplicatePosition(positionName));
	});
	$('#addPositionForm button[type="submit"]').prop('disabled', true);

	// Update model box function event
	$('#positionUpdateName').on('input', function(event) {
		var positionId = $('#positionId').val();
		var positionName = $(this).val().trim();
		var updateButton = $('#updatePositionForm button[type="submit"]');

		// Disable the button if positionName is blank or if it's a duplicate
		updateButton.prop('disabled', positionName === '' || /^\s*$/.test(positionName) ||  positionName.length <= 2 || positionName.length > 255 || checkDuplicatePositionUpdate(positionId, positionName));
	});

	$("[id^=btnOK]").on("click", function(event) {
		event.preventDefault(); // Prevent the default behavior (page reload)

		// Get all open modals with class "modal"
		var openModals = $(".modal.show");

		// Close all open modals
		openModals.modal("hide");
	});




	// Click event for update buttons
	$('#positionTable').on('click', '.edit-btn', function() {
		var positionId = $(this).data('id');
		var positionName = $(this).closest('tr').find('td:first-child').text();

		// Update the modal title and set the position name in the modal body
		$('#positionUpdateModalLabel').text('Position - Update');
		$('#positionUpdateName').val(positionName); // Update the input field with position name
		$('#positionId').val(positionId);

		// Show the position modal
		$('#positionUpdateModal').modal('show');
	});

	// Clear the position modal on close
	$('#positionUpdateModal').on('hidden.bs.modal', function() {
		$('#positionUpdateModalLabel').text('Position - Update');
		$('#positionUpdateName').val('');
		$('#error-msg-update').text('').hide();
		$('#updatePositionForm button[type="submit"]').prop('disabled', true);
	});


});