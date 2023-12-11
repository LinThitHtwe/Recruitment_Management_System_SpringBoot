$(document).ready(function() {

	var baseAjaxUrl = '/hr/interviews-data';
	// Function to load users with the specified page and row count
	function loadUsers(page, rowCount) {
		// Your logic to load users with the given page and row count
		// Update the DataTable with the new data
		$('#userTable').DataTable().ajax.reload();
	}
	function loadPositionsForSort(targetSelect) {
		$.ajax({
			url: "/hr/position-for-sort",
			type: "GET",
			success: function(response) {
				var positionSelect = $(targetSelect);

				// Clear any existing options
				positionSelect.empty();

				// Add options for each position
				positionSelect.append('<option value="all">All</option>');
				response.forEach(function(position) {
					var option = $("<option>")
						.val(position.name) // Use position name as the value
						.text(position.name);
					positionSelect.append(option);
				});
			},
			error: function(xhr, status, error) {
				console.error(error);
			}
		});
	}

	function loadDepartmentForSort(targetSelect) {
		$.ajax({
			url: "/hr/department-for-sort",
			type: "GET",
			success: function(response) {
				var departmentSelect = $(targetSelect);

				// Clear any existing options
				departmentSelect.empty();

				// Add options for each position
				departmentSelect.append('<option value="all">All</option>');
				response.forEach(function(department) {
					var option = $("<option>")
						.val(department.name) // Use position name as the value
						.text(department.name);
					departmentSelect.append(option);
				});
			},
			error: function(xhr, status, error) {
				console.error(error);
			}
		});
	}

	// Load positions on initial page load
	loadPositionsForSort("#positionSort");
	loadDepartmentForSort("#departmentSort");


	function filterData(type, status, stage, position, department) {
		var table = $('#interviewTable').DataTable();
		var typeFilter = type === 'all' ? '' : type;
		var statusFilter = status === 'all' ? '' : status === 'true' ? true : false;
		var stageFilter = stage === 'all' ? '' : stage;
		var positionFilter = position === 'all' ? '' : position;
		var departmentFilter = department === 'all' ? '' : department;
		table.search('').columns().search('').draw();

		table.column(0).search(positionFilter, true, false);
		table.column(1).search(departmentFilter, true, false);
		table.column(4).search(typeFilter);
		table.column(6).search(statusFilter);
		table.column(5).search(stageFilter);

		// Redraw the table to apply the filters
		table.draw();
	}

	// Attach the event handlers
	// Type, Status, Stage, and Position filter change events
	$('#typeSelect, #statusSelect, #stageSelect, #positionSort,#departmentSort').on('change', function() {
		var selectedType = $('#typeSelect').val();
		var selectedStatus = $('#statusSelect').val();
		var selectedStage = $('#stageSelect').val();
		var selectedPosition = $('#positionSort').val();
		var selectedDepartment = $('#departmentSort').val();
		filterData(selectedType, selectedStatus, selectedStage, selectedPosition, selectedDepartment);
	});

	// Initialize DataTable
	var dataTable = $('#interviewTable').DataTable({
		processing: true,
		serverSide: true,
		ajax: {
			url: baseAjaxUrl,
			type: 'GET',
		},
		columns: [
			{ data: 'vacancy.position.name', name: 'Position' },
			{ data: 'vacancy.department.name', name: 'Department' },

			{
				data: 'startDate', name: 'Start Date',
				render: function(data) {
					if (data) {
						const date = new Date(data);
						const day = String(date.getDate()).padStart(2, '0');
						const month = String(date.getMonth() + 1).padStart(2, '0');
						const year = date.getFullYear();
						return `${day}-${month}-${year}`;
					}
					return '';
				},
			},

			{
				data: 'endDate', name: 'End Date',
				render: function(data) {
					if (data) {
						const date = new Date(data);
						const day = String(date.getDate()).padStart(2, '0');
						const month = String(date.getMonth() + 1).padStart(2, '0');
						const year = date.getFullYear();
						return `${day}-${month}-${year}`;
					}
					return '-';
				},
			},
			{ data: 'type', name: 'Type' },
			{
				data: 'stage',
				name: 'Stage',
				render: function(data) {
					if (data === 1) {
						return 'Stage 1';
					} else if (data === 2) {
						return 'Stage 2';
					} else if (data === 3) {
						return 'Stage 3';
					}
					else if (data === 4) {
						return 'Stage 4';
					}
					else {
						return 'Unknown';
					}
				},
				sortable: false
			},
			{
				data: 'status',
				name: 'Status',
				render: function(data, type, row) {
					if (data == false) {
						if (row.canceledUserId !== 0) {
							return 'Canceled';
						}
						else {
							return 'Expired';
						}
					}
					else {
						return 'Active';
					}
				},
				sortable: false
			},

			{
				data: 'id',
				render: function(data, type, row) {
					var infoButton = `<button title="Interview Info" type="button" class="btn btn-info info-btn" data-id="${row.id}" id="infoButton"><i class="ti ti-info"></i></button>`;
					var editButton = '';
					if (row.status) {
						editButton = `<button title="Edit Interview" type="button" class="btn btn-primary edit-btn" data-id="${row.id}"><i class="ti ti-pencil"></i></button>`;
					} else {
						editButton = '';
					}



					return `<div class="btn-group">${infoButton}${editButton}</div>`;
				},
				sortable: false
			},
			{
				data: 'id',
				render: function(data, type, row) {
					var cancelButton = '';
					if (row.status) {
						cancelButton = `<button title="Inactive Interview" type="button" class="btn btn-danger cancel-btn" data-id="${row.id}"><i class="bi bi-trash-fill"></i></button>`;
					} else {
						return '<div class="dash">-</div>';
					}
					return `<div class="btn-group">${cancelButton}</div>`;
				},
				sortable: false
			},

		],
		order: [[2, 'desc']],
		paging: true,
		lengthMenu: [5, 10, 20], // Number of records per page
		pageLength: 5, // Default number of records per page
	});

	$('#startDateFrom, #endDateTo').on('change', function() {
		var startDateFrom = $('#startDateFrom').val();
		var endDateTo = $('#endDateTo').val();

		if (!startDateFrom || !endDateTo) {
			console.log("At least one date is selected, redrawing the table.");

			if (!startDateFrom) {
				table.column(3).search(endDateTo);
			} else {
				table.column(2).search(startDateFrom);
			}

			if (!endDateTo) {
				table.column(2).search(startDateFrom);
			} else {
				table.column(3).search(endDateTo);
			}

			dataTable.draw();
		} else {
			// Clear the filter if end date becomes higher than start date
			if (new Date(endDateTo) > new Date(startDateFrom)) {
				clearEndDateFilter();
			}

			console.log("Both start and end dates are selected.");
			updateDataTable(startDateFrom, endDateTo);
		}
	});



	function redrawTable() {
		dataTable.draw();
	}

	function clearFilter() {
		console.log("Clearing filter.");
		dataTable.column(2).search("").draw();
		dataTable.column(3).search("").draw();
	}

	function clearEndDateFilter() {
		console.log("Clearing end date filter.");
		dataTable.column(3).search("").draw();
	}

	function updateDataTable(startDate, endDate) {
		// Update the URL with both start and end dates
		dataTable.ajax.url(baseAjaxUrl + '?startDateFrom=' + startDate + '&endDateTo=' + endDate).load();
	}

	$('#resetButton').on('click', function() {
		var table = $('#interviewTable').DataTable();
		table.search('').columns().search('').draw();
		$('#stageSelect').val('all');
		$('#typeSelect').val('all');
		$('#statusSelect').val('all');
		$('#positionSort').val('all');
		$('#departmentSort').val('all');
		$('#startDateFrom').val('');
		$('#endDateTo').val('');
		dataTable.ajax.reload();

	});

});

var mySelectAdd = document.getElementById("add-userId");
var searchInputAdd = document.getElementById("add-searchInput");

searchInputAdd.addEventListener("keyup", function(event) {
	var searchTextAdd = event.target.value.toLowerCase();
	var optionsAdd = mySelectAdd.options;
	for (let i = 0; i < optionsAdd.length; i++) {
		var optionTextAdd = optionsAdd[i].text.toLowerCase();
		if (optionTextAdd.includes(searchTextAdd)) {
			optionsAdd[i].style.display = "block";
		}
		else {
			optionsAdd[i].style.display = "none";
		}
	}
});

$(document).ready(function() {
	$('#add-userId').mousedown(function(e) {
		e.preventDefault();
		var originalScrollTopAdd = $(this).scrollTop();
		$(this).focus();
		var mouseDownYAdd = e.pageY;
		var $thisAdd = $(this);
		var lastSelectedAdd = null;

		$thisAdd.children('option').each(function() {
			var thisPosAdd = $(this).offset().top - originalScrollTopAdd;
			var topPosAdd = thisPosAdd + $thisAdd.scrollTop();
			var bottomPosAdd = topPosAdd + $(this).outerHeight();

			if (mouseDownYAdd >= topPosAdd && mouseDownYAdd <= bottomPosAdd) {
				if (e.shiftKey && lastSelectedAdd) {
					var startAdd = $thisAdd.children('option').index(lastSelectedAdd);
					var endAdd = $thisAdd.children('option').index(this);

					$thisAdd.children('option').slice(Math.min(start, end), Math.max(startAdd, endAdd) + 1).prop('selected', true);

				}
				else {
					$(this).prop('selected', !$(this).prop('selected'));
					lastSelectedAdd = this;
				}
			}
		})
	})
});

function clearSelectedOptionsAdd() {
	var selectElementAdd = document.getElementById("add-userId");
	for (var i = 0; i < selectElementAdd.options.length; i++) {
		selectElementAdd.options[i].selected = false;
	}
}

//multi select for add end

//multi select for update
var mySelect = document.getElementById("update-userId");
var searchInput = document.getElementById("searchInput");

searchInput.addEventListener("keyup", function(event) {
	var searchText = event.target.value.toLowerCase();
	var options = mySelect.options;
	for (let i = 0; i < options.length; i++) {
		var optionText = options[i].text.toLowerCase();
		if (optionText.includes(searchText)) {
			options[i].style.display = "block";
		}
		else {
			options[i].style.display = "none";
		}
	}
});

$(document).ready(function() {
	$('#update-userId').mousedown(function(e) {
		e.preventDefault();
		var originalScrollTop = $(this).scrollTop();
		$(this).focus();
		var mouseDownY = e.pageY;
		var $this = $(this);
		var lastSelected = null;

		$this.children('option').each(function() {
			var thisPos = $(this).offset().top - originalScrollTop;
			var topPos = thisPos + $this.scrollTop();
			var bottomPos = topPos + $(this).outerHeight();

			if (mouseDownY >= topPos && mouseDownY <= bottomPos) {
				if (e.shiftKey && lastSelected) {
					var start = $this.children('option').index(lastSelected);
					var end = $this.children('option').index(this);

					$this.children('option').slice(Math.min(start, end), Math.max(start, end) + 1).prop('selected', true);

				}
				else {
					$(this).prop('selected', !$(this).prop('selected'));
					lastSelected = this;
				}
			}
		})
	})
});


function clearSelectedOptions() {
	var selectElement = document.getElementById("update-userId");
	for (var i = 0; i < selectElement.options.length; i++) {
		selectElement.options[i].selected = false;
	}
}

//multi select for update end

// Set "Start Date" input to the current date for add
var currentDate = new Date();
var startDateInput = document.getElementById("add-startDate");
var endDateInput = document.getElementById("add-endDate");
var updateStartDateInput = document.getElementById("startDate");
var updateEndDateInput = document.getElementById("endDate");
startDateInput.min = currentDate.toISOString().split("T")[0];
endDateInput.min = currentDate.toISOString().split("T")[0];

// Add event listener to update "End Date" input for add
startDateInput.addEventListener("input", function() {
	var selectedStartDate = new Date(startDateInput.value);
	var minEndDate = new Date(selectedStartDate.getTime() + 86400000); // Add 1 day in milliseconds

	if (minEndDate > new Date(endDateInput.value)) {
		endDateInput.value = "";
	}

	endDateInput.min = minEndDate.toISOString().split("T")[0];
});

updateStartDateInput.addEventListener("input", function() {
	var selectedStartDate = new Date(updateStartDateInput.value);

	var minEndDate = new Date(selectedStartDate.getTime() + 86400000);

	if (minEndDate > new Date(updateEndDateInput.value)) {
		updateEndDateInput.value = "";
	}

	updateEndDateInput.min = minEndDate.toISOString().split("T")[0];

})




//for add-interview
function validateFormFields() {
	var add_vacancy = document.getElementById("add-vacancy");
	var add_startDate = document.getElementById("add-startDate");
	var add_endDate = document.getElementById("add-endDate");
	var add_startTime = document.getElementById("add-startTime");
	var add_endTime = document.getElementById("add-endTime");
	var add_type = document.getElementById("add-type");
	var add_location = document.getElementById("add-location");
	var add_stage = document.getElementById("add-stage");
	var add_interviewer = document.getElementById("add-userId");
	var timeDifferenceInMinutes;
	var startTimeLaterThanCurrentTime = false;
	var invalidTime = false;
	var startTimeValue = add_startTime.value;
	var endTimeValue = add_endTime.value;

	// Reset error messages
	resetErrorMessages();

	if (add_startTime.value.trim() !== "") {
		var currentDate = new Date(); 
		var selectedStartDate = new Date(add_startDate.value);


		if (selectedStartDate.toDateString() === currentDate.toDateString()) {
			console.log("If-selectedStartDate-" + selectedStartDate);
			console.log("Current-date" + currentDate);
			var currentTime = new Date();
			var selectedStartTime = new Date(`${currentDate.toISOString().split('T')[0]}T${add_startTime.value}`);
			console.log("selectedStartDate-" + selectedStartDate);
			console.log("selectedStartTIme-" + selectedStartTime);
			if (add_endDate.value.trim() == "" || add_endDate.value.trim() == null) {
				if (selectedStartTime <= currentTime) {
					console.log("Current-time" + currentTime);
					console.log("startTimeLaterThanCurrentTime");
					startTimeLaterThanCurrentTime = true;
				}
			}

		}


	}

	if ((startTimeValue < '08:00' || startTimeValue >= '19:00') || (endTimeValue < '08:00' || endTimeValue >= '19:00')) {
		console.log("invalidTime");
		invalidTime = true;
	}

	if (add_startTime.value.trim() !== "" && add_endTime.value.trim() !== "") {
		var startTime = new Date(`2000-01-01T${add_startTime.value}`);
		var endTime = new Date(`2000-01-01T${add_endTime.value}`);
		var timeDifferenceInMinutes = (endTime - startTime) / (1000 * 60);
	}

	if (add_vacancy.value === "" || add_startDate.value.trim() === "" || add_startTime.value.trim() === "" || add_endTime.value.trim() === "" || add_stage.value.trim() === "" || /^\s+$/.test(add_stage.value) || (add_type.value === "In-person" && (add_location.value.trim() === "" || /^\s+$/.test(add_location.value))) || add_interviewer.value.length === 0 || timeDifferenceInMinutes < 30 || startTimeLaterThanCurrentTime == true || invalidTime == true) {

		//validate vacancy
		if (add_vacancy.value === "") {
			displayErrorMessage(add_vacancy, "Please choose a vacancy.");
		}

		// Validate start date
		if (add_startDate.value.trim() === "") {
			displayErrorMessage(add_startDate, "Please enter start date.");
			//return false;
		}

		// Validate start time
		if (add_startTime.value.trim() === "") {
			displayErrorMessage(add_startTime, "Please enter start time.");
			// return false;
		}

		// Validate end time
		if (add_endTime.value.trim() === "") {
			displayErrorMessage(add_endTime, "Please enter end time.");
			//return false;
		}

		// Validate stage
		if (add_stage.value.trim() === "" || /^\s+$/.test(add_stage.value)) {
			displayErrorMessage(add_stage, "Please enter valid interview stage.");
		}

		// Validate location if type is "in-person"
		if (add_type.value === "In-person" && (add_location.value.trim() === "" || /^\s+$/.test(add_location.value))) {
			displayErrorMessage(add_location, "Please enter valid location.");
		}

		//validate interviewers
		if (add_interviewer.value.length === 0) {
			displayErrorMessage(add_interviewer, "Please choose at lease one interviewer.");
		}
		if (invalidTime == true) {
			displayErrorMessage(add_startTime, "Time Should be between Worktime hours 8am-7pm");
		}

		if (startTimeLaterThanCurrentTime == true) {
			displayErrorMessage(add_startTime, "Start time must be later than current time.");
		}

		// Check if end time is earlier than start time
		if (timeDifferenceInMinutes < 30) {
			displayErrorMessage(add_endTime, "End time must be at least 30 minutes later than start time.");
		}
		return false;
	}

	// Validation passed
	return true;
}

function validateUpdateFormFields() {
	var vacancy = document.getElementById("update-vacancy");
	var startDate = document.getElementById("startDate");
	var startTime = document.getElementById("startTime");
	var endTime = document.getElementById("endTime");
	var type = document.getElementById("type");
	var location = document.getElementById("location");
	var stage = document.getElementById("stage");
	var interviewer = document.getElementById("update-userId");
	var timeDifferenceInMinutes;
	var invalidTime = false;
	var startTimeValue = startTime.value;
	var endTimeValue = endTime.value;
	var currentDate = new Date();
	// Reset error messages
	resetErrorMessages();

	if (startTime.value.trim() !== "" && endTime.value.trim() !== "") {
		var update_startTime = new Date(`${currentDate.toISOString().split('T')[0]}T${startTime.value}`);
		var update_endTime = new Date(`${currentDate.toISOString().split('T')[0]}T${endTime.value}`);
		
		
		var timeDifferenceInMinutes = (update_endTime - update_startTime) / (1000 * 60);

	}

	if ((startTimeValue < '08:00' || startTimeValue >= '19:00') || (endTimeValue < '08:00' || endTimeValue >= '19:00')) {
		console.log("invalidTime");
		invalidTime = true;
	}

	if (vacancy.value === "" || startDate.value.trim() === "" || startTime.value.trim() === "" || endTime.value.trim() === "" || stage.value.trim() === "" || /^\s+$/.test(stage.value) || (type.value === "In-person" && (location.value.trim() === "" || /^\s+$/.test(location.value))) || interviewer.value.length === 0 || timeDifferenceInMinutes < 30 || invalidTime) {

		//validate vacancy
		if (vacancy.value === "") {
			displayErrorMessage(vacancy, "Please choose a vacancy.");
		}

		// Validate start date
		if (startDate.value.trim() === "") {
			displayErrorMessage(startDate, "Please enter start date.");
		}

		// Validate start time
		if (startTime.value.trim() === "") {
			displayErrorMessage(startTime, "Please enter start time.");
		}

		// Validate end time
		if (endTime.value.trim() === "") {
			displayErrorMessage(endTime, "Please enter end time.");
		}

		// Validate stage
		if (stage.value.trim() === "" || /^\s+$/.test(stage.value)) {
			displayErrorMessage(stage, "Please enter valid interview stage.");
		}

		// Validate location if type is "Onsite"
		if (type.value === "In-person" && (location.value.trim() === "" || /^\s+$/.test(location.value))) {
			displayErrorMessage(location, "Please enter valid location.");
		}

		//validate interviewers
		if (interviewer.value.length === 0) {
			displayErrorMessage(interviewer, "Please choose at lease one interviewer.");
		}

		if (invalidTime == true) {
			displayErrorMessage(startTime, "Time Should be between Worktime hours 8am-7pm");
		}

		// Check if end time is earlier than start time
		if (timeDifferenceInMinutes < 30) {
			displayErrorMessage(endTime, "End time must be at lease 30 minutes later than start time.");
		}
		return false;
	}


	// Validation passed
	return true;
}

function displayErrorMessage(inputField, message) {
	var errorDiv = document.createElement("div");
	errorDiv.className = "error-message";
	errorDiv.innerHTML = message;

	// Apply color to the error message
	errorDiv.style.color = "red";

	// Append error message after the input field
	inputField.parentNode.insertBefore(errorDiv, inputField.nextSibling);

	// Add error class to input field
	inputField.classList.add("error");

	// Clear error message after 3 seconds
	setTimeout(function() {
		errorDiv.parentNode.removeChild(errorDiv);
		inputField.classList.remove("error");
	}, 3000); // 3000 milliseconds = 3 seconds
}


//clear duplicate errors for add
function clearAddDuplicateErrorMessages() {
	var add_type = document.getElementById("add-type");
	var add_stage = document.getElementById("add-stage");
	var add_vacancy = document.getElementById("add-vacancy");

	var errorDiv_type = document.getElementById("errorDiv_addtype");
	var errorDiv_stage = document.getElementById("errorDiv_addstage");
	var errorDiv_vacancy = document.getElementById("errorDiv_addvacancy");

	if (errorDiv_type) {
		errorDiv_type.parentNode.removeChild(errorDiv_type);
	}

	if (errorDiv_stage) {
		errorDiv_stage.parentNode.removeChild(errorDiv_stage);
	}

	if (errorDiv_vacancy) {
		errorDiv_vacancy.parentNode.removeChild(errorDiv_vacancy);
	}

	// Remove error class from input fields
	add_type.classList.remove("error");
	add_stage.classList.remove("error");
	add_vacancy.classList.remove("error");
}

//clear duplicate errors for add
function clearUpdateDuplicateErrorMessages() {
	var update_type = document.getElementById("type");
	var update_stage = document.getElementById("stage");
	var update_vacancy = document.getElementById("update-vacancy");

	var errorDiv_type = document.getElementById("errorDiv_updatetype");
	var errorDiv_stage = document.getElementById("errorDiv_updatestage");
	var errorDiv_vacancy = document.getElementById("errorDiv_updatevacancy");

	if (errorDiv_type) {
		errorDiv_type.parentNode.removeChild(errorDiv_type);
	}

	if (errorDiv_stage) {
		errorDiv_stage.parentNode.removeChild(errorDiv_stage);
	}

	if (errorDiv_vacancy) {
		errorDiv_vacancy.parentNode.removeChild(errorDiv_vacancy);
	}

	// Remove error class from input fields
	update_type.classList.remove("error");
	update_stage.classList.remove("error");
	update_vacancy.classList.remove("error");
}


// Error message for duplication for add
function displayDuplicateErrorMessageAdd() {
	var add_type = document.getElementById("add-type");
	var add_stage = document.getElementById("add-stage");
	var add_vacancy = document.getElementById("add-vacancy");

	// Create error div for each field
	var errorDiv_type = createErrorDiv("Interview has already been  created for the selected vacancy, stage and type.");
	errorDiv_type.id = "errorDiv_addtype";
	var errorDiv_stage = createErrorDiv("Interview has already been  created for the selected vacancy, stage and type.");
	errorDiv_stage.id = "errorDiv_addstage";
	var errorDiv_vacancy = createErrorDiv("Interview has already been  created for the selected vacancy, stage and type.");
	errorDiv_vacancy.id = "errorDiv_addvacancy";

	// Append error messages after the corresponding input fields
	add_type.parentNode.insertBefore(errorDiv_type, add_type.nextSibling);
	add_stage.parentNode.insertBefore(errorDiv_stage, add_stage.nextSibling);
	add_vacancy.parentNode.insertBefore(errorDiv_vacancy, add_vacancy.nextSibling);

	// Add error class to input fields
	add_type.classList.add("error");
	add_stage.classList.add("error");
	add_vacancy.classList.add("error");


	// Clear error messages when input fields are clicked
	add_type.addEventListener("input", function() {
		removeErrorDiv(errorDiv_type, add_type);
		removeErrorDiv(errorDiv_stage, add_stage);
		removeErrorDiv(errorDiv_vacancy, add_vacancy);

	});

	add_stage.addEventListener("input", function() {
		removeErrorDiv(errorDiv_type, add_type);
		removeErrorDiv(errorDiv_stage, add_stage);
		removeErrorDiv(errorDiv_vacancy, add_vacancy);
	});

	add_vacancy.addEventListener("input", function() {
		removeErrorDiv(errorDiv_type, add_type);
		removeErrorDiv(errorDiv_stage, add_stage);
		removeErrorDiv(errorDiv_vacancy, add_vacancy);
	});
}

// Error message for duplication for update
function displayDuplicateErrorMessageUpdate() {
	var type = document.getElementById("type");
	var stage = document.getElementById("stage");
	var vacancy = document.getElementById("update-vacancy");

	// Create error div for each field
	var errorDiv_type = createErrorDiv("Interview has already been created for the selected vacancy, stage and type.");
	errorDiv_type.id = "errorDiv_updatetype";
	var errorDiv_stage = createErrorDiv("Interview has already been created for the selected vacancy, stage and type.");
	errorDiv_stage.id = "errorDiv_updatestage";
	var errorDiv_vacancy = createErrorDiv("Interview has already been created for the selected vacancy, stage and type.");
	errorDiv_vacancy.id = "errorDiv_updatevacancy";

	// Append error messages after the corresponding input fields
	type.parentNode.insertBefore(errorDiv_type, type.nextSibling);
	stage.parentNode.insertBefore(errorDiv_stage, stage.nextSibling);
	vacancy.parentNode.insertBefore(errorDiv_vacancy, vacancy.nextSibling);

	// Add error class to input fields
	type.classList.add("error");
	stage.classList.add("error");
	vacancy.classList.add("error");

	// Clear error messages when input fields are clicked
	type.addEventListener("input", function() {
		removeErrorDiv(errorDiv_type, type);
		removeErrorDiv(errorDiv_stage, stage);
		removeErrorDiv(errorDiv_vacancy, vacancy);
	});

	stage.addEventListener("input", function() {
		removeErrorDiv(errorDiv_type, type);
		removeErrorDiv(errorDiv_stage, stage);
		removeErrorDiv(errorDiv_vacancy, vacancy);
	});

	vacancy.addEventListener("input", function() {
		removeErrorDiv(errorDiv_type, type);
		removeErrorDiv(errorDiv_stage, stage);
		removeErrorDiv(errorDiv_vacancy, vacancy);
	});
}

function createErrorDiv(message) {
	var errorDiv = document.createElement("div");
	errorDiv.className = "duplicate-error";
	errorDiv.innerHTML = message;
	errorDiv.style.color = "red";
	return errorDiv;
}

function removeErrorDiv(errorDiv, inputField) {
	if (errorDiv.parentNode) {
		errorDiv.parentNode.removeChild(errorDiv);
	}
	inputField.classList.remove("error");
}


function resetErrorMessages() {
	var errorMessages = document.getElementsByClassName("error-message");
	var inputFields = document.getElementsByClassName("formbold-form-input");

	// Remove error messages
	while (errorMessages.length > 0) {
		errorMessages[0].parentNode.removeChild(errorMessages[0]);
	}

	// Remove error class from input fields
	for (var i = 0; i < inputFields.length; i++) {
		inputFields[i].classList.remove("error");
	}


}

//when onsite is clicked, location disappear (add form)
window.addEventListener("DOMContentLoaded", function() {
	toggleAddress();
});

function toggleAddress() {
	var typeDropdown = document.getElementById("add-type");
	var locationInput = document.getElementById("add-locationDiv");

	if (typeDropdown.value === "In-person") {
		locationInput.style.display = "block";
	} else {
		locationInput.style.display = "none";
	}
}

//when onsite is clicked, location disappear (update form)
// Function to handle the visibility of the location input field
function toggleAddressUpdate() {
	var typeDropdown = document.getElementById("type");
	var locationInput = document.getElementById("locationDiv");

	if (typeDropdown.value === "In-person") {
		locationInput.style.display = "block";
	} else {
		locationInput.style.display = "none";
	}
}

// Trigger the initial toggle when the DOM content is loaded
document.addEventListener("DOMContentLoaded", function() {
	// Call the toggleAddressUpdate function initially
	toggleAddressUpdate();

	// Add event listener to the type dropdown
	var typeDropdown = document.getElementById("type");
	typeDropdown.addEventListener("change", toggleAddressUpdate);
});

// Call the toggleAddressUpdate function when the update modal is shown
$('#interviewUpdateModal').on('shown.bs.modal', function() {
	toggleAddressUpdate();
});


//fill vacancy options for add
$(document).ready(function() {
	// Add event listener to the button with the interview-update-btn class
	$('.interview-add-button').click(function() {
		// Call the function and pass the interview ID
		getVacancies();
	});
});

// Event listener for change in vacancy selection
$('#add-vacancy').on('change', function() {
	var selectedVacancyId = $(this).val();
	if (selectedVacancyId) {
		// Call the function to fetch users for the selected vacancy's position
		getUsersForVacancyPosition(selectedVacancyId);
	} else {
		// Clear the users select field if no vacancy is selected
		$('#add-userId').empty();
	}
});



//for interview update form
function getVacancies() {
	$.ajax({
		url: "/hr/vacancies-for-interview",
		type: "Post",
		success: function(response) {
			$('#add-vacancy').empty();
			// Add the default option
			$('#add-vacancy').append('<option value="" disabled selected>Select Vacancy</option>');
			response.forEach(function(vacancy) {
				// Create a new option element
				var option = $('<option></option>');

				// Set the value and text of the option using vacancy's id and position name
				option.val(vacancy.id);

				//change to dd-mm-yyyy format
				var createdDate = new Date(vacancy.createdDate);
				var dueDate = new Date(vacancy.dueDate);

				var createdDateString = createdDate.getDate().toString().padStart(2, '0') + "." +
					(createdDate.getMonth() + 1).toString().padStart(2, '0') + "." +
					createdDate.getFullYear();

				var dueDateString = dueDate.getDate().toString().padStart(2, '0') + "." +
					(dueDate.getMonth() + 1).toString().padStart(2, '0') + "." +
					dueDate.getFullYear();

				option.text(vacancy.position.name + " " + createdDateString + " - " + dueDateString + " " +
					vacancy.department.name);

				// Append the option to the select element
				$('#add-vacancy').append(option);

			});

			// Show interviewAddModal after populating the options
			$('#interviewAddModal').modal('show');
		},
		error: function(xhr, status, error) {
			console.error(error);
		}

	});
}

// Function to fetch users for the selected vacancy's position
function getUsersForVacancyPosition(selectedVacancyId) {
	$.ajax({
		url: "/hr/choose-interviwers",
		type: "GET",
		data: { vacancyId: selectedVacancyId },
		success: function(response) {
			// Populate the users select field
			$('#add-userId').empty();

			if (response.length > 0) {
				response.forEach(function(user) {
					var userOption = $('<option></option>');
					userOption.val(user.id);
					userOption.text(user.name);
					$('#add-userId').append(userOption);
				});
			}
		},
		error: function(xhr, status, error) {
			console.error(error);
		}
	});
}

function formatDateToString(date) {
	const year = date.getFullYear();
	const month = String(date.getMonth() + 1).padStart(2, '0');
	const day = String(date.getDate()).padStart(2, '0');
	return `${year}-${month}-${day}`;
}

//check interview duplation for add
$('#add-stage').on('change', function(event) {
	var interviewAddBtn = document.getElementById("interviewAddBtn");
	var selectedStage = $(this).val();
	var selectedType = document.getElementById("add-type").value;
	var selectedVacancy = document.getElementById("add-vacancy").value;
	if (selectedStage != "" && selectedType != "" && selectedVacancy != "") {
		const data = {
			type: selectedType,
			stage: selectedStage,
			vacancyId: selectedVacancy
		};


		$.ajax({
			type: 'GET',
			url: '/hr/check-interview-add-duplication',
			data: data,
			success: function(response) {
				if (response == "duplicated") {
					displayDuplicateErrorMessageAdd();
					interviewAddBtn.disabled = true;
				}
				else {
					interviewAddBtn.disabled = false;
				}

			},
			error: function(xhr, status, error) {
				console.log(xhr.responseText);
			}
		});
	}
});

//check interview duplation for add
$('#add-type').on('change', function(event) {
	var selectedType = $(this).val();
	var selectedStage = document.getElementById("add-stage").value;
	var selectedVacancy = document.getElementById("add-vacancy").value;
	if (selectedStage != "" && selectedType != "" && selectedVacancy != "") {
		const data = {
			type: selectedType,
			stage: selectedStage,
			vacancyId: selectedVacancy
		};

		$.ajax({
			type: 'GET',
			url: '/hr/check-interview-add-duplication',
			data: data,
			success: function(response) {
				if (response == "duplicated") {
					displayDuplicateErrorMessageAdd();
					interviewAddBtn.disabled = true;

				} else {
					interviewAddBtn.disabled = false;
				}
			},
			error: function(xhr, status, error) {
				console.log(xhr.responseText);
			}
		});
	}
});

//check interview duplation for add
$('#add-vacancy').on('change', function(event) {
	var interviewAddBtn = document.getElementById("interviewAddBtn");
	var selectedVacancy = $(this).val();
	var selectedType = document.getElementById("add-type").value;
	var selectedStage = document.getElementById("add-stage").value;
	if (selectedStage != "" && selectedType != "" && selectedVacancy != "") {
		const data = {
			type: selectedType,
			stage: selectedStage,
			vacancyId: selectedVacancy
		};

		$.ajax({
			type: 'GET',
			url: '/hr/check-interview-add-duplication',
			data: data,
			success: function(response) {
				if (response == "duplicated") {
					displayDuplicateErrorMessageAdd();
					interviewAddBtn.disabled = true;

				} else {
					interviewAddBtn.disabled = false;
				}
			},
			error: function(xhr, status, error) {
				console.log(xhr.responseText);
			}
		});
	}
});

//check interview duplation for update
$('#update-vacancy').on('change', function() {
	var interviewUpdateBtn = document.getElementById("interviewUpdateBtn");
	var selectedVacancy = $(this).val();
	var selectedType = document.getElementById("type").value;
	var selectedStage = document.getElementById("stage").value;
	var interviewId = document.getElementById("id").value;
	if (selectedStage != "" && selectedType != "" && selectedVacancy != "" && interviewId != "") {
		const data = {
			type: selectedType,
			stage: selectedStage,
			vacancyId: selectedVacancy,
			interviewId: interviewId
		};

		$.ajax({
			type: 'GET',
			url: '/hr/check-interview-update-duplication',
			data: data,
			success: function(response) {
				if (response == "duplicated") {
					displayDuplicateErrorMessageUpdate();
					interviewUpdateBtn.disabled = true;
				}
				else {
					interviewUpdateBtn.disabled = false;
				}
			},
			error: function(xhr, status, error) {
				console.log(xhr.responseText);
			}
		});
	}
});

//check interview duplation for update
$('#stage').on('change', function(event) {
	var interviewUpdateBtn = document.getElementById("interviewUpdateBtn");
	var selectedStage = $(this).val();
	var selectedType = document.getElementById("type").value;
	var selectedVacancy = document.getElementById("update-vacancy").value;
	var interviewId = document.getElementById("id").value;
	if (selectedStage != "" && selectedType != "" && selectedVacancy != "" && interviewId != "") {
		const data = {
			type: selectedType,
			stage: selectedStage,
			vacancyId: selectedVacancy,
			interviewId: interviewId
		};

		$.ajax({
			type: 'GET',
			url: '/hr/check-interview-update-duplication',
			data: data,
			success: function(response) {
				if (response == "duplicated") {
					displayDuplicateErrorMessageUpdate();
					interviewUpdateBtn.disabled = true;
				}
				else {
					interviewUpdateBtn.disabled = false;
				}
			},
			error: function(xhr, status, error) {
				console.log(xhr.responseText);
			}
		});
	}
});


//check interview duplation for update
$('#type').on('change', function(event) {
	var interviewUpdateBtn = document.getElementById("interviewUpdateBtn");
	var selectedType = $(this).val();
	var selectedStage = document.getElementById("stage").value;
	var selectedVacancy = document.getElementById("update-vacancy").value;
	var interviewId = document.getElementById("id").value;
	if (selectedStage != "" && selectedType != "" && selectedVacancy != "" && interviewId != "") {
		const data = {
			type: selectedType,
			stage: selectedStage,
			vacancyId: selectedVacancy,
			interviewId: interviewId
		};

		$.ajax({
			type: 'GET',
			url: '/hr/check-interview-update-duplication',
			data: data,
			success: function(response) {
				if (response == "duplicated") {
					displayDuplicateErrorMessageUpdate();
					interviewUpdateBtn.disabled = true;
				}
				else {
					interviewUpdateBtn.disabled = false;
				}
			},
			error: function(xhr, status, error) {
				console.log(xhr.responseText);
			}
		});
	}
});



function submitForm() {
	console.log($('#add-startDate').val());
	console.log($('#add-endDate').val());
	var startDateValue = formatDateToString(new Date($('#add-startDate').val()));
	var endDateValue = formatDateToString(new Date($('#add-endDate').val()));
	console.log(startDateValue);
	console.log(endDateValue);
	$.ajax({
		type: 'POST',
		url: '/hr/add-interview',
		/*      data: $("#interviewAddForm").serialize() + "&startDate=" + startDateValue + "&endDate=" + endDateValue,*/
		data: $("#interviewAddForm").serialize(),
		success: function(response) {
			$('#interviewAddModal').modal('hide');
			var interviewTable = $('#interviewTable').DataTable();
			interviewTable.ajax.reload();
			$('#successAlert').show();
			setTimeout(function() {
				$('#successAlert').hide();
			}, 3000);

		},
		error: function(xhr, status, error) {
			console.log(xhr.responseText);
		}
	});

	return false;
}




//clear add form when close or cancel button is clicked
function resetAddForm() {
	$("#interviewAddForm")[0].reset();
	$("#error-msg").text('');
	resetErrorMessages();
	clearAddDuplicateErrorMessages();

}


//get interview id for showInterviewDetail function
$(document).ready(function() {
	// Add event listener to the info button(s) that trigger the modal
	$(document).on('click', '.info-btn', function() {
		// Get the interview ID from the data-id attribute of the info button
		var interviewId = $(this).data('id');

		// Call the function and pass the interview ID
		showInterviewDetail(interviewId);
	});
});


function formatDate(date) {
	var year = date.getFullYear();
	var month = (date.getMonth() + 1).toString().padStart(2, '0'); // Month is zero-based
	var day = date.getDate().toString().padStart(2, '0');
	return day + '-' + month + '-' + year;
}

function formatDate2(inputDate) {
	// Parse the input date string into a Date object
	const parts = inputDate.split('-');
	const year = parseInt(parts[0]);
	const month = parseInt(parts[1]) - 1;
	const day = parseInt(parts[2]);

	const dateObject = new Date(year, month, day);

	// Extract day, month, and year components
	const formattedDay = String(dateObject.getDate()).padStart(2, '0');
	const formattedMonth = String(dateObject.getMonth() + 1).padStart(2, '0');
	const formattedYear = String(dateObject.getFullYear());

	// Create the formatted date string
	const formattedDate = `${formattedDay}-${formattedMonth}-${formattedYear}`;

	return formattedDate;
}
function showInterviewDetail(interviewId) {

	$.ajax({
		url: "/hr/interview-detail",
		type: "GET",
		data: { id: interviewId },
		success: function(response) {
			//pass interview details
			$('#positionPlaceholder').text(response.vacancy.position.name);
			$('#departmentPlaceholder').text(response.vacancy.department.name);

			var createdDate = new Date(response.vacancy.createdDate);
			var dueDate = new Date(response.vacancy.dueDate);
			var vacancyDate = "From " + formatDate(createdDate) + " To " + formatDate(dueDate);
			$('#vacancyDatePlaceholder').text(vacancyDate);

			$('#startDatePlaceholder').text(formatDate2(response.startDate));
			$('#endDatePlaceholder').text(response.endDate !== null ? formatDate2(response.endDate) : '-');
			$('#startTimePlaceholder').text(response.startTime);
			$('#endTimePlaceholder').text(response.endTime);
			$('#typePlaceholder').text(response.type);
			$('#stagePlaceholder').text(response.stage);
			$('#locationPlaceholder').text(response.location !== null ? response.location : '-');
			if (!response.status) {
				if (response.canceledUsername == null) {
					$('#statusPlaceholder').text('EXPIRED');
				} else {
					$('#statusPlaceholder').text('CANCELED');
				}
			} else {
				$('#statusPlaceholder').text('VALID');
			}
			$('#createdUserPlaceholder').text(response.createdUserName);
			$('#createdDateTimePlaceholder').text(response.createdDateTime);
			$('#updatedUserPlaceholder').text(response.updatedUserName !== null ? response.updatedUserName : '-');
			$('#updatedDateTimePlaceholder').text(response.updatedDateTime !== null ? response.updatedDateTime : '-');
			$('#canceledUserPlaceholder').text(response.canceledUsername !== null ? response.canceledUsername : '-');
			$('#canceledDateTimePlaceholder').text(response.canceledDateTime !== null ? response.canceledDateTime : '-');
			// Format and display the list of user names with commas
			var userNames = response.users.map(function(user) {
				return user.name;
			});
			$('#interviewerPlaceholder').text(userNames.join(', '));


			// Show the detail modal
			$('#interviewDetailModal').modal('show');

		},
		error: function(xhr, status, error) {
			console.error(error);
		}

	});
}

$(document).ready(function() {
	// Add event listener to the button with the edit-btn class
	$(document).on('click', '.edit-btn', function() {
		// Get the interview ID from the data-id attribute of the edit button
		console.log("on click edit btn");
		var interviewId = $(this).data('id');
		getVacanciesForUpdate(interviewId);
	});
});

// Event listener for change in vacancy selection for update
$('#update-vacancy').on('change', function() {
	var selectedVacancyId = $(this).val();
	var interviewId = $('#id').val();
	
	$.ajax({
		url: "/hr/interview-detail",
		type: "GET",
		data: { id: interviewId },
		success: function(response) {
			getUsersForVacancyPositionUpdate(selectedVacancyId)
				.then(function(usersResponse) {
					// Populate the users select field with the new options
					$('#update-userId').empty();
					if (usersResponse.length > 0) {
						usersResponse.forEach(function(user) {
							var userOption = $('<option></option>');
							userOption.val(user.id);
							userOption.text(user.name);
							$('#update-userId').append(userOption);
						});
					}

					// Check if the selectedVacancyId matches the response.vacancy.id
					if (selectedVacancyId == response.vacancy.id) {
						// Loop through the users and select the ones from the response.users array
						response.users.forEach(function(user) {
							$('#update-userId option[value="' + user.id + '"]').prop('selected', true);
						});
					} else {
						// If the selected vacancy is different, select "All Users" as default
						$('#update-userId').val(""); // Clear any previous selections
					}
				})
				.catch(function(error) {
					console.error(error);
				});
		},
		error: function(xhr, status, error) {
			console.error(error);
		}
	});
});



function getVacanciesForUpdate(interviewId) {
	
	$.ajax({
		url: "/hr/vacancies-for-interview-update",
		type: "POST",
		data: { interviewId: interviewId },
		success: function(response) {
			// Clear existing options and populate the select element
			var vacancySelect = $('#update-vacancy');
			vacancySelect.empty();

			// Add the default option
			vacancySelect.append('<option value="" disabled selected>Select Vacancy</option>');

			// Add the new options from the response
			response.forEach(function(vacancy) {
				var option = $('<option></option>');
				option.val(vacancy.id);


				var createdDate = new Date(vacancy.createdDate);
				var dueDate = new Date(vacancy.dueDate);

				var createdDateString = createdDate.getDate().toString().padStart(2, '0') + "." +
					(createdDate.getMonth() + 1).toString().padStart(2, '0') + "." +
					createdDate.getFullYear();

				var dueDateString = dueDate.getDate().toString().padStart(2, '0') + "." +
					(dueDate.getMonth() + 1).toString().padStart(2, '0') + "." +
					dueDate.getFullYear();

				option.text(vacancy.position.name + " " + createdDateString + " - " + dueDateString + " " +
					vacancy.department.name);

				vacancySelect.append(option);
			});

			initializeInterviewUpdate(interviewId);
		},
		error: function(xhr, status, error) {
			console.error(error);
			reject(error);
		}
	});

}
function initializeInterviewUpdate(interviewId) {
	// Get the current date
	var currentDate = new Date().toISOString().split('T')[0]; // Format: "YYYY-MM-DD"

	// Fetch interview details and populate the update form fields
	$.ajax({
		url: "/hr/interview-detail",
		type: "GET",
		data: { id: interviewId },
		success: function(response) {
			// Populate update form fields
			$('#id').val(response.id);
			$('#update-vacancy').val(response.vacancy.id);
			var startDateValue = new Date(response.startDate);

			// Set min attribute for startDate input
			if (response.startDate > currentDate) {
				console.log("if");
				$('#startDate').attr('min',currentDate);
			} else {
				$('#startDate').attr('min',  response.startDate);
				console.log("else");
			}

			var dayAfterStartDate = new Date(startDateValue);
			dayAfterStartDate.setDate(dayAfterStartDate.getDate() + 1);
			$('#endDate').attr('min', dayAfterStartDate.toISOString().split('T')[0]);


			$('#startDateInputUpdate').val(response.startDate);
			$('#startDate').val(response.startDate);
			$('#endDate').val(response.endDate);
			$('#startTime').val(response.startTime);
			$('#endTime').val(response.endTime);
			$('#stage').val(response.stage);
			$('#type').val(response.type);
			$('#location').val(response.location);

			getUsersForVacancyPositionUpdate($('#update-vacancy').val())
				.then(function(usersResponse) {
					$('#update-userId').empty();
					if (usersResponse.length > 0) {
						usersResponse.forEach(function(user) {
							var userOption = $('<option></option>');
							userOption.val(user.id);
							userOption.text(user.name);
							$('#update-userId').append(userOption);
						});
					}

					// Loop through the users and select the ones from the response.department.users array
					response.users.forEach(function(user) {
						$('#update-userId option[value="' + user.id + '"]').prop('selected', true);
					});

					$('#interviewUpdateModal').modal('show');
				})
				.catch(function(error) {
					console.error(error);
				});
		},
		error: function(xhr, status, error) {
			console.error(error);
		}
	});
}


function getUsersForVacancyPositionUpdate(selectedVacancyId) {
	return new Promise(function(resolve, reject) {
		if (selectedVacancyId) {
			$.ajax({
				url: "/hr/choose-interviwers",
				type: "GET",
				data: { vacancyId: selectedVacancyId },
				success: function(response) {
					resolve(response); // Resolve the Promise with the response data
				},
				error: function(xhr, status, error) {
					console.error(error);
					reject(error);
				}
			});
		} else {
			// If no vacancy is selected, resolve the Promise with an empty array
			resolve([]);
		}
	});
}

//to update interview
$(document).ready(function() {
	console.log("------")
	$('#interviewUpdateForm').submit(function(event) {
		event.preventDefault();
	console.log("---submit update---");
		console.log("Result of validateUpdateFormFields(): " + validateUpdateFormFields());
		if (validateUpdateFormFields()) {
			console.log("submit");
			var formData = $(this).serialize();
			updateInterview(formData);
			$('#interviewUpdateModal').modal('hide');
			console.log("success");
			var interviewTable = $('#interviewTable').DataTable();
			interviewTable.ajax.reload();
			$('#successAlert').show();
			setTimeout(function() {
				$('#successAlert').hide();
			}, 3000);
		}
	});
});

function updateInterview(formData) {
	console.log("updateInterview__");
	$.ajax({
		url: "/hr/update-interview",
		type: "POST",
		data: formData,
		success: function(response) {
			$('#interviewUpdateModal').modal('hide');
			console.log("success");
			var interviewTable = $('#interviewTable').DataTable();
			interviewTable.ajax.reload();
			$('#successAlert').show();
			setTimeout(function() {
				$('#successAlert').hide();
			}, 3000);

		},
		error: function(xhr, status, error) {
			console.error(error);
		}

	});

}

// Close the modal box when the user clicks on the close button (x)
$('.close').click(function() {
	$('#interviewModal').css('display', 'none');
});


//clear update form when close or cancel button is clicked
function resetUpdateForm() {
	$("#interviewUpdateForm")[0].reset();
	$("#error-msg").text('');
	resetErrorMessages();
	clearUpdateDuplicateErrorMessages();

}

//show cancel confirmation modal
$(document).ready(function() {
	$(document).on('click', '.cancel-btn', function() {
		// Get the interview ID from the data-id attribute of the cancel button
		var interviewId = $(this).data('id');
		showConfirmationModal(interviewId);
	});
});

function showConfirmationModal(interviewId) {
	$('#interviewConfirmationModal').find('[data-interview-id]').attr('data-interview-id', interviewId);
	$('#interviewConfirmationModal').modal('show');

}

function performCancelAction() {
	var interviewId = $('#interviewConfirmationModal').find('[data-interview-id]').attr('data-interview-id');
	cancelInterview(interviewId);

}
function cancelInterview(interviewId) {
	$.ajax({
		url: "/hr/cancel-interview",
		type: "GET",
		data: { id: interviewId },
		success: function(response) {
			$('#cancelSuccessModal').modal('show');
			$('#interviewConfirmationModal').modal('hide');
		}
	});
}