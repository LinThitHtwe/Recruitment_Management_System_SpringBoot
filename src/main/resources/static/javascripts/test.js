$(document).ready(function() {
	var table = $('#candidateTable').DataTable({
		processing: true,
		serverSide: true,
		ajax: {
			url: '/hr/testcon',
			type: 'GET',
		},

		language: {
			emptyTable: "No data found"
		},
		columns: [
			{
				data: null,
				orderable: false,
				searchable: false,
				render: function(data, type, row) {
					return (
						'<input type="checkbox" class="candidate-checkbox" name="id" value="' +
						row.id +
						'" id="candidateCheckbox-' +
						row.id +
						'">'
					);
				},
				responsivePriority: 1,
			},
			{ data: "name", responsivePriority: 2 },
			{ data: "vacancy.position.name", responsivePriority: 3 },
			{ data: "mainTech", responsivePriority: 4 },
			{
				data: 'submitDate',
				responsivePriority: 5,
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
			{ data: "exp" },
			{
				data: null,
				targets: 6,
				searchable: false,
				sortable: false,
				visible: true,
				render: function(data, type, row) {
					var lastStatus = row.candidateStatusList[row.candidateStatusList.length - 1];
					if (lastStatus) {
						var statusName = lastStatus.status.name;
						var statusClass;
						if (statusName === "Received") {
							statusClass = "status-received";
						} else if (statusName === "Viewed") {
							statusClass = "status-viewed";
						} else if (statusName === "Considering") {
							statusClass = "status-considering";
						} else {
							statusClass = "";
						}
						return '<div class="status-badge ' + statusClass + '">' + statusName + '</div>';
					}
					return '';
				}

			},
			{
				data: "candidateStatusList",
				render: function(data, type, row) {
					var lastStatus = data[data.length - 1];
					var statusOptions;

					if (lastStatus) {
						var statusName = lastStatus.status.name;
						var candidateId = row.id;

						if (statusName === "Received") {
							statusOptions = '<button class="btn btn-success status-btn" data-candidate-id="' + candidateId + '" data-status-id="2" data-status-name="Viewed">Level Up</button>';
						} else if (statusName === "Viewed") {
							statusOptions = '<button class="btn btn-success status-btn" data-candidate-id="' + candidateId + '" data-status-id="3" data-status-name="Considering">Level Up</button>';
						} else if (statusName === "Considering") {
							statusOptions = '<span style="text-align: center; display: inline-block; width: 100%;">-</span>';
						} else {
							statusOptions = '';
						}

						return `
              ${statusName !== "Considering" ? statusOptions : '<span style="text-align: center; display: inline-block; width: 100%;">-</span>'}
            `;
					}
					return '';
				},
				sortable: false, responsivePriority: 7
			},
			{
				data: "candidateInterviews",
				render: function(data, type, row) {
					var lastStatus = data[data.length - 1];
					if (lastStatus) {
						var statusName = lastStatus.status;
						var statusClass;
						if (statusName === "Reached") {
							statusClass = "status-received";
						} else if (statusName === "Pending") {
							statusClass = "status-viewed";
						} else if (statusName === "Passed") {
							statusClass = "status-considering";
						} else if (statusName === "Cancel") {
							statusClass = "status-decline";
						} else {
							statusClass = ""; // Add a default class if needed
						}
						return '<div class="status-badge ' + statusClass + '">' + statusName + '</div>';
					}
					return '<div class="dash">-</div>';
				},
				sortable: false,
				responsivePriority: 8
			},
			{
				data: "candidateInterviews",
				render: function(data, type, row) {
					var lastStatus = data[data.length - 1];
					if (lastStatus) {
						var statusName = lastStatus.interview.stage;
						return '<div class="status-badge stage-info"> Stage ' + statusName + '</div>';
					}
					return '<div class="dash">-</div>';
				},
				sortable: false,
				responsivePriority: 9
			},

			{
				data: "vacancy.id",
				//data: "",
				render: function(data, type, row) {
					var candidateInterviews = row.candidateInterviews;
					var candidateStatusList = row.candidateStatusList;
					var lastSelectionStatus = candidateStatusList[candidateStatusList.length - 1];
					var lastStatus = candidateInterviews[candidateInterviews.length - 1];
					var statusOptions = '';

					var candidateId = row.id;
					var candidateName = row.name;

					var vacancyId = row.vacancy.id;

					var isEmploy = row.isEmploy ? "Employed" : "Not Employed";
					var isMailSent = row.isMailSent ? "Sent" : "UnSent";

					if (lastStatus && lastStatus.status && lastStatus.status !== "Passed" && lastStatus.status !== "Reached" && lastStatus.status !== "Cancel") {
						statusOptions = '<select class="form-control status-select status-select-class" data-candidate-id="' + candidateId + '" data-vacancy-id="' + vacancyId + '" data-candidate-name="' + candidateName + '">' +
							'<option value="" selected disabled>Select Interview Type</option>' +
							'<option value="Online">Online</option>' +
							'<option value="Onsite">Onsite</option>' +
							'</select>';
					} else if (lastStatus == null && lastSelectionStatus && lastSelectionStatus.status.name === "Considering") {
						statusOptions = '<select class="form-control status-select status-select-class" data-candidate-id="' + candidateId + '" data-vacancy-id="' + vacancyId + '" data-candidate-name="' + candidateName + '">' +
							'<option value="" selected disabled>Select Interview Type</option>' +
							'<option value="Online">Online</option>' +
							'<option value="Onsite">Onsite</option>' +
							'</select>';
					}
					else {
						if (isMailSent === "Sent" && isEmploy === "Employed") {
							statusOptions = '<div class="status-badge status-considering">Employed</div>';

						}

						else if (lastStatus && lastStatus.status === "Passed" && isMailSent === "UnSent") {
							statusOptions = '<div class="dash"><button class="btn btn-success job-offer-btn" data-candidate-id="' + candidateId + '"><i class="bi bi-envelope-fill"></i></button></div>';
						} else if (lastStatus && lastStatus.status === "Passed" && isMailSent === "Sent") {
							statusOptions = '<div class="dash"><button class="btn btn-success employ-btn" data-candidate-id="' + candidateId + '" data-candidate-name="' + candidateName + '"><i class="bi bi-person-fill-add"></i></button></div>';
						}
						else {

							statusOptions = '<div class="dash">-</div>';
						}
					}

					return statusOptions;
				},
				sortable: false,
				responsivePriority: 9
			},


			{
				data: "id",
				render: function(data, type, row) {
					var infoButton = `<button type="button" class="btn btn-info info-btn" data-id="${row.id}" id="infoButton"><i class="ti ti-info"></i></button>`;

					// Add the new button with the icon and action URL
					var historyButton = `<a href="/hr/candidate-history/${row.id}" class="btn btn-primary"><i class="bi bi-clock-history text-white"></i></a>`;

					return `<div class="btn-group">${infoButton} ${historyButton}</div>`;
				},
				sortable: false, responsivePriority: 10
			},
		],
		order: [[1, 'asc']],
		paging: true,
		lengthMenu: [1, 2, 20], // Number of records per page
		pageLength: 1, // Default number of records per page
		initComplete: function() {
			$("#selectionStatusSelect").trigger("change");
		},
		select: {
			style: "multi", // Enable multi-row selection
			selector: "td:first-child input[type=checkbox]:not(.info-btn input[type=checkbox])",
		},
		responsive: true,
	});


	$("#selectionStatusSelect").on("change", function() {
		var selectedStatus = $(this).val();
		if (selectedStatus === "all") {
			selectedStatus = ""; 
		}
		table.column(6).search(selectedStatus).draw();
	});
	function updateCandidateStatus(candidateId, newStatusId) {
		$.ajax({
			url: '/hr/changeSelectionStatus',
			type: 'GET',
			data: { candidateId: candidateId, statusId: newStatusId },
			success: function() {
				// If the update was successful, show the Bootstrap alert notification
				$('#successAlert').show();

				// Hide the alert after 3 seconds (3000 milliseconds)
				setTimeout(function() {
					$('#successAlert').hide();
				}, 3000);

				// Refresh the table data using AJAX
				table.ajax.reload();
			},
			error: function() {
				// Handle error if needed
				alert('Failed to update candidate status.');
				// Revert the table data to its original state
				table.ajax.reload();
			}
		});
	}

	$(document).on('click', '.employ-btn', function() {
		var candidateName = $(this).data('candidate-name');
		$('#candidateNameEmploy').text(candidateName);
		$('#employConfirmationModal').modal('show');

		// Store the candidateId and candidateName in variables to use in the confirmation
		var candidateId = $(this).data('candidate-id');
		var candidateName = $(this).data('candidate-name');

		// When the user clicks "OK" in the modal, trigger the employCandidate function with the candidateId
		$('#confirmEmploy').click(function() {
			employCandidate(candidateId);
		});
	});

	function employCandidate(candidateId) {
		// Make the POST request using jQuery AJAX
		$.ajax({
			type: 'POST',
			url: '/hr/employ-candidate',
			data: { candidateId: candidateId },
			success: function(response) {
				// If the update was successful, show the Bootstrap alert notification
				$('#successAlert').show();

				// Hide the alert after 3 seconds (3000 milliseconds)
				setTimeout(function() {
					$('#successAlert').hide();
				}, 3000);

				// Refresh the table data using AJAX
				table.ajax.reload();

				// Handle the success response if needed
				console.log('Employment successful.');
			},
			error: function(xhr, status, error) {
				// Handle the error response if needed
				console.error('Error while employing candidate:', error);
			}
		});

		// Hide the modal after the confirmation is done
		$('#employConfirmationModal').modal('hide');
	}


	$(document).on('click', '.job-offer-btn', function(e) {
		e.preventDefault(); // Prevents the default behavior of the anchor link or button
		var type = ""
		var stage = 0
		var button = $(this);
		var candidateId = button.data('candidate-id');
		var url = "/hr/email-prepare-form/5" + "?type=" + type + "&candidateId=" + candidateId + "&stage=" + stage;

		window.location.href = url;
	});


	// Event handler for the status buttons
	$(document).on('click', '.status-btn', function(e) {
		var button = $(this);
		var candidateId = button.data('candidate-id');
		var newStatusId = button.data('status-id');
		var newStatusName = button.data('status-name');

		// Set the status name in the modal
		$('#statusName').text(newStatusName);

		// Show the confirmation modal
		$('#confirmationModal').modal('show');

		// Set the data attributes for the "OK" button in the modal
		$('#confirmStatusChange').data('candidate-id', candidateId);
		$('#confirmStatusChange').data('status-id', newStatusId);

		// Prevent default behavior of checkbox selection
		e.stopPropagation();
	});

	// Event handler for the status change confirmation
	$(document).on('click', '#confirmStatusChange', function(e) {
		// Hide the confirmation modal
		$('#confirmationModal').modal('hide');

		// Get the selected status ID from the data attribute
		var candidateId = $(this).data('candidate-id');
		var newStatusId = $(this).data('status-id');

		// Update the candidate status on the server
		updateCandidateStatus(candidateId, newStatusId);

		// Prevent default behavior of checkbox selection
		e.stopPropagation();
	}); confirmCloseX

	// Event handler for the "Cancel" button in the confirmation modal
	$(document).on('click', '#cancelStatusChange', function() {
		// Hide the confirmation modal
		$('#confirmationModal').modal('hide');
		table.ajax.reload();
	});
	// Event handler for the "Cancel" button in the confirmation modal
	$(document).on('click', '#confirmCloseX', function() {
		// Hide the confirmation modal
		$('#confirmationModal').modal('hide');
		table.ajax.reload();
	});

	$('#confirmationModal').on('hidden.bs.modal', function(e) {

		table.ajax.reload();
	});

	$('#warningModal').on('click', '.close', function() {
		$('#warningModal').modal('hide');
		table.ajax.reload();
	});

	$('#warningModal').on('click', '#closeBtn', function() {
		$('#warningModal').modal('hide');
		table.ajax.reload();
	});

	$('#warningModal').on('hidden.bs.modal', function() {
		table.ajax.reload();
	});

	$('#successModal').on('click', '.close', function() {
		$('#successModal').modal('hide');
		table.ajax.reload();
	});

	$('#successModal').on('click', '#closeBtn', function() {
		$('#successModal').modal('hide');
		table.ajax.reload();
	});

	$('#successModal').on('hidden.bs.modal', function() {
		table.ajax.reload();
	});


	$("#candidateTable tbody").on("click", "tr", function() {
		var checkbox = $(this).find(".candidate-checkbox");
		var isSelected = checkbox.prop("checked");
		checkbox.prop("checked", !isSelected);
		table.row(this).select(!isSelected);
		updateCheckboxState();
	});

	// Event listener for DataTable selection change
	table.on("select deselect", function() {
		updateCheckboxState();
	});

	// Function to update checkbox state based on DataTable selection
	function updateCheckboxState() {
		var selectedRows = table.rows({ selected: true }).nodes();
		var checkboxes = $(".candidate-checkbox", selectedRows);
		var allCheckboxes = $(".candidate-checkbox", table.rows().nodes());

		// Check/uncheck the checkboxes based on DataTable selection
		allCheckboxes.prop("checked", false);
		checkboxes.prop("checked", true);
	}

	// Click event listener for the "Download" button
	$("#downloadButton").on("click", function() {
		var selectedIDs = table
			.rows({ selected: true })
			.data()
			.map(function(item) {
				return item.id;
			});

		if (selectedIDs.length > 0) {
			var downloadURL = "/candidate/downloadCv?id=" + selectedIDs.join(",");
			window.location.href = downloadURL;
		} else {
			alert("Please select candidates to download CVs.");
		}
	});

	$("#candidateTable tbody").on("click", ".info-btn", function() {
		var rowId = $(this).data("id");

		// Make the AJAX call to get candidate details
		$.ajax({
			url: "/hr/getCandidateInfo",
			type: "GET",
			data: { id: rowId },
			success: function(response) {
				// Update the 'updateModel' content with the received candidate details
				updateUpdateModelContent(response);
				// Show the 'updateModel'
				$("#updateModel").modal("show");
			},
			error: function(xhr, status, error) {
				// Handle the error, e.g., show an error message
				console.log("Error fetching candidate details:", error);
			}
		});
	});

	// Function to update the content of 'updateModel'
	function updateUpdateModelContent(candidateData) {
		// Assuming 'updateModel' has a <div> with class 'modal-body' to display the content
		var modalBody = $("#updateModel .modal-body");
		// Customize how you want to display the candidate details in 'updateModel'

		// Determine the gender value to display
		var genderDisplay = candidateData.gender === true ? "Female" : "Male";

		// Combine submitDate and submitTime to show submitted date and time
		var submittedDateTime = candidateData.submitDate + " " + candidateData.submitTime;

		var content = `
      <p>Name: ${candidateData.name}</p>
      <p>Date of Birth: ${candidateData.dob}</p>
      <p>Gender: ${genderDisplay}</p>
      <p>Phone: ${candidateData.phone}</p>
      <p>Education: ${candidateData.education}</p>
      <p>Email: ${candidateData.email}</p>
      <p>Technical Skill: ${candidateData.techSkill}</p>
      <p>Language Skill: ${candidateData.languageSkill}</p>
      <p>Main Technology: ${candidateData.mainTech}</p>
      <p>Experience: ${candidateData.exp}</p>
      <p>Level: ${candidateData.level}</p>
      <p>Expected Salary: ${candidateData.expectedSalary}</p>
      <p>Submitted Date and Time: ${submittedDateTime}</p>
    `;
		modalBody.html(content);
	}

	// Hide the warning modal
	function hideWarningModal() {
		$('#warningModal').modal('hide');
	}


	$(document).on('change', '.status-select-class', function() {
		var candidateId = $(this).data('candidate-id');
		var candidateName = $(this).data('candidate-name');
		var vacancyId = $(this).data('vacancy-id');
		var selectedOption = $(this).val();
		var templateId = (selectedOption === 'online') ? '4' : '3';

		$.ajax({
			type: "GET",
			url: "/hr/interview-stages",
			data: {
				candidateId: candidateId
			},
			success: function(response) {
				var stage = response;
				console.log(stage);
				$.ajax({
					type: "GET",
					url: "/hr/choose-interview",
					data: {
						interviewType: selectedOption,
						candidateId: candidateId,
						vacancyId: vacancyId,
						stage: stage
					},
					success: function(response) {
						$("#candidateName").text(candidateName);
						$("#successModal").modal("show");
						console.log(response);

						var emailPrepFormUrl = "/hr/email-prepare-form/" + templateId + "?type=" + selectedOption + "&candidateId=" + candidateId + "&stage=" + stage;

						$("#sendEmailBtn").on("click", function() {
							window.location.href = emailPrepFormUrl;
						});

					},
					error: function(error) {
						console.log(error.responseText); // Log the error response
						var errorMessage = error.responseText;
						showWarningModal(errorMessage);
					}
				});
			},
			error: function(error) {
				console.log(error.responseText); // Log the error response
				var errorMessage = error.responseText;
				showWarningModal(errorMessage);
			}
		});
		// Show the warning modal with the error message
		function showWarningModal(errorMessage) {
			$("#warningMessage").text(errorMessage);
			$('#warningModal').modal('show');
		}
	});

});
