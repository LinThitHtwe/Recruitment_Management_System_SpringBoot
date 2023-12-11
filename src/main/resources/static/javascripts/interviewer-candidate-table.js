$(document).ready(function() {
	var baseAjaxUrl = '/interviewer/candidate-data';

	function loadPositionsForSort(targetSelect) {

		$.ajax({
			url: "/interviewer/position-for-sort",
			type: "GET",
			success: function(response) {
				var positionSelect = $(targetSelect);
				positionSelect.empty();
				positionSelect.append('<option value="all">All</option>');
				response.forEach(function(position) {
					var option = $("<option>")
						.val(position.name)
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
			url: "/interviewer/department-for-sort",
			type: "GET",
			success: function(response) {
				var departmentSelect = $(targetSelect);

				departmentSelect.empty();

				departmentSelect.append('<option value="all">All</option>');
				response.forEach(function(department) {
					var option = $("<option>")
						.val(department.name)
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


	function filterData(position, department) {
		var table = $('#candidateTable').DataTable();
		var positionFilter = position === 'all' ? '' : position;
		var departmentFilter = department === 'all' ? '' : department;
		table.search('').columns().search('').draw();

		table.column(1).search(positionFilter, true, false);
		table.column(2).search(departmentFilter, true, false);

		// Redraw the table to apply the filters
		table.draw();
	}

	// Attach the event handlers
	// Type, Status, Stage, and Position filter change events
	$('#positionSort,#departmentSort').on('change', function() {
		var selectedPosition = $('#positionSort').val();
		var selectedDepartment = $('#departmentSort').val();
		filterData(selectedPosition, selectedDepartment);
	});

	const $reviewText = $('#reviewText');
	const $submitReviewButton = $('#submitReviewButton');

	// Add an input event listener to the textarea
	$reviewText.on('input', function() {
		const reviewContent = $reviewText.val().trim();

		// Enable or disable the submit button based on the review content length
		if (reviewContent.length < 2) {
			$submitReviewButton.prop('disabled', true);
		} else {
			$submitReviewButton.prop('disabled', false);
		}
	});

	// Initialize the button state on page load
	$submitReviewButton.prop('disabled', true);



	var table = $('#candidateTable').DataTable({
		processing: true,
		serverSide: true,
		ajax: {
			url: baseAjaxUrl,
			type: 'GET',
		},
		columns: [
			{ data: "name", responsivePriority: 2 },
			{ data: "vacancy.position.name", responsivePriority: 3 },
			{ data: "vacancy.department.name" },
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
			{ data: "exp", responsivePriority: 6 },

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
					return '';
				},
				sortable: false, responsivePriority: 7
			},

			{
				data: "candidateInterviews",
				render: function(data, type, row) {
					var candidateInterviews = row.candidateInterviews;
					var lastIndex = candidateInterviews.length - 1;
					var lastStatus = lastIndex >= 0 ? candidateInterviews[lastIndex].status : "";
					candidateInterviews.forEach(intv => console.log(intv.status));
					var lastStatus = data[data.length - 1];

					if (lastStatus) {
						var statusName = lastStatus.interview.stage;

						return '<div class="status-badge stage-info"> stage ' + statusName + '</div>';
					}
					return '';
				},
				sortable: false,
				responsivePriority: 8
			},
			{
				data: "id",
				render: function(data, type, row) {
					var currentUserRole;

					$.ajax({
						url: '/interviewer/current-user-role',
						type: 'GET',
						async: false,
						success: function(role) {
							currentUserRole = role;
						},
						error: function() {
							currentUserRole = "";
						}
					});

					var lastStatus = row.candidateInterviews[row.candidateInterviews.length - 1];

					var statusName = lastStatus ? lastStatus.status : "";
					console.log(lastStatus.interview.startDate);
					var formattedStartDate = new Date(
						lastStatus.interview.startDate[0],
						lastStatus.interview.startDate[1] - 1,
						lastStatus.interview.startDate[2]
					);

					var year = formattedStartDate.getFullYear();
					var month = (formattedStartDate.getMonth() + 1).toString().padStart(2, '0');
					var day = formattedStartDate.getDate().toString().padStart(2, '0');
					var formattedStartDateFinal = `${year}-${month}-${day}`;
					console.log(formattedStartDateFinal);

					var formattedEndDate = null;
					var formattedEndDateFinal = null;
					if (lastStatus.interview.endDate != null) {
						console.log("endDate not null");
						var formattedEndDate = new Date(
							lastStatus.interview.endDate[0],
							lastStatus.interview.endDate[1] - 1,
							lastStatus.interview.endDate[2]
						);
						var endyear = formattedEndDate.getFullYear();
						var endmonth = (formattedEndDate.getMonth() + 1).toString().padStart(2, '0');
						var endday = formattedEndDate.getDate().toString().padStart(2, '0');
						formattedEndDateFinal = `${endyear}-${endmonth}-${endday}`;

						console.log(lastStatus.interview.endDate);
						formattedEndDate = new Date(lastStatus.interview.endDate[0], lastStatus.interview.endDate[1], lastStatus.interview.endDate[2]).toISOString().split('T')[0];
						console.log("Finalll--"+formattedEndDateFinal);
					}
					var changeStatusButton = `<button  title="Set Interview Status" type="button" class="btn btn-warning warning-btn" data-id="${row.id}" data-start-date="${formattedStartDateFinal}" data-end-date="${formattedEndDateFinal}" id="changeStatusButton"><i class="bi bi-person-fill-gear"></i></button>`;
					var reviewButton = `<button title="Write Review" type="button" class="btn btn-info review-btn" data-candidate-id="${row.id}" data-toggle="modal" data-target="#reviewModal"><i class="ti ti-pencil"></i></button>`;
					var content = '<div class="btn-group">' + reviewButton;

					if ((currentUserRole === 'Department-head' || currentUserRole === 'Admin' || currentUserRole === 'Default-Admin') && statusName === "Reached") {
						content += changeStatusButton;
					}
					content += '</div>';
					return content;
				},
				sortable: false,
				responsivePriority: 9
			},
			{
				data: "id",
				render: function(data, type, row) {
					var currentUserRole;

					$.ajax({
						url: '/interviewer/current-user-role',
						type: 'GET',
						async: false,
						success: function(role) {
							currentUserRole = role;
						},
						error: function() {
							currentUserRole = "";
						}
					});


					var infoButton = `<button title="Candidate's Info" type="button" class="btn btn-info info-btn" data-id="${row.id}" id="infoButton"><i class="bi bi-person-lines-fill"></i></button>`;
					var reviewedText = `<a title="Review Lists" href="/department-head/candidate/${row.id}" class="btn btn-secondary"><i class="bi bi-book"></i></a>`;

					var historyButton = `<a title="Candidate History" href="/interviewer/candidate-history/${row.id}" class="btn btn-primary"><i class="bi bi-clock-history text-white"></i></a>`;
					var historyButtonAdmin = `<a title="Candidate History" href="/hr/candidate-history/${row.id}" class="btn btn-primary"><i class="bi bi-clock-history text-white"></i></a>`;
					var content = '<div class="btn-group">' + infoButton;
					if ((currentUserRole === 'Admin' || currentUserRole === 'Default-Admin')) {
						content += historyButtonAdmin;
					}
					if ((currentUserRole === 'Department-head' || currentUserRole === 'Interviewer')) {
						content += historyButton;
					}
					if ((currentUserRole === 'Department-head' || currentUserRole === 'Admin' || currentUserRole === 'Default-Admin')) {
						content += reviewedText;
					}
					content += '</div>';
					return content;
				},
				sortable: false, responsivePriority: 10
			},
		],
		order: [[4, 'desc']],
		paging: true,
		lengthMenu: [10, 15, 20],
		pageLength: 10,


		responsive: true,
		language: {
			"infoFiltered": ""
		}

	});


	$('#interviewStatusSelect,#interviewStageSelect').on('change', function() {

		var interviewStatus = $('#interviewStatusSelect').val();
		var stage = $('#interviewStageSelect').val();
		table.ajax.url(baseAjaxUrl + '?&interviewStatus=' + interviewStatus + '&stage=' + stage).load();

	});

	$("#resetButton").click(function() {

		$("#interviewStatusSelect").val("All");

		$("#interviewStageSelect").val("All");

		$("#positionSort").val("all");
		$("#departmentSort").val("all");

		$("#startDateFrom").val("");
		$("#endDateTo").val("");
		table.clear().draw();
		table.columns().search('').draw();
		table.ajax.url(baseAjaxUrl).load();
	});


	$(document).on('click', '.review-btn', function() {
		var candidateId = $(this).data('candidate-id');
		$('#reviewModal').data('candidateId', candidateId);
	});

	$('#submitReviewButton').click(function() {
		var reviewText = $('#reviewText').val();
		var candidateId = $('#reviewModal').data('candidateId');

		if (candidateId) {
			$.ajax({
				type: 'GET',
				url: '/interviewer/save-reviews',
				data: {
					review: reviewText,
					candidateId: candidateId
				},
				success: function() {
			
				$('#reviewModal').removeClass("show");
               $('.modal-backdrop').hide();
					$('#successAlert').show();

					setTimeout(function() {
						$('#successAlert').hide();
					}, 3000);

					table.ajax.reload();
				},
				error: function(error) {
					console.log(error.responseText);
				}
			});
		} else {
			console.log("Candidate ID is not available. Please make sure to set it correctly.");
		}
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
	});

	// Event handler for the "Cancel" button in the confirmation modal
	$(document).on('click', '#cancelStatusChange', function() {
		// Hide the confirmation modal
		$('#confirmationModal').modal('hide');
	});

	$(document).on('click', '.close,.btn-secondary', function() {
		$('.show').modal('hide');

	});

	// Attach click event to changeStatusButton elements (using delegated event handler)
	$(document).on('click', '#changeStatusButton', function() {
		// Get the data-id, startDate, and endDate from the clicked button
		const dataId = $(this).data('id');
		const startDate = $(this).data('startDate');
		const endDate = $(this).data('endDate');

		// Show the modal
		$('#statusModal').modal('show');

		// Set the data-id attribute for the confirmButton
		$('#confirmButton').data('id', dataId);

		// Update the min and max attributes of the date picker
		const interviewDateInput = $('#interviewDate');
		console.log(startDate);
		interviewDateInput.attr('min', startDate);

		if (endDate) {
			console.log("if enddate");
			console.log(endDate);
			interviewDateInput.attr('max', endDate);
		} else {
			interviewDateInput.attr('max', startDate);
		}
	});



	$('#confirmButton').click(function() {
		const selectedStatus = $('input[name="status"]:checked').val();
		const interviewDate = $('#interviewDate').val();
		const candidateId = $(this).data('id');
		if (selectedStatus && interviewDate) {
			$.ajax({
				type: 'GET',
				url: '/interviewer/get-interview-id',
				data: {
					candidateId: candidateId
				},
				success: function(interviewId) {
					// Perform actions based on the selected status and obtained interviewId
					if (selectedStatus && interviewId) {
						$.ajax({
							type: 'GET',
							url: '/interviewer/set-interview-status',
							data: {
								status: selectedStatus,
								candidateId: candidateId,
								interviewId: interviewId,
								date: interviewDate
							},
							success: function() {
								$('#reviewModal').modal('hide');

								$('#successAlert').show();

								setTimeout(function() {
									$('#successAlert').hide();
								}, 3000);

								table.ajax.reload();
							},
							error: function(error) {
								console.log(error.responseText); // Log the error response
								// Handle error if any.
							}
						});

						$('#statusModal').modal('hide');
					}
				},
				error: function(error) {
					console.log(error.responseText); // Log the error response
					// Handle error if any.
				}
			});
		} else {
			$('#errorMessage').show();

			// Hide the error message after 3 seconds
			setTimeout(function() {
				$('#errorMessage').hide();
			}, 3000);

			// Disable the button to prevent submission


		}
	});

	$("#candidateTable tbody").on("click", ".info-btn", function() {
		var rowId = $(this).data("id");

		$.ajax({
			url: "/interviewer/getCandidateInfo",
			type: "GET",
			data: { id: rowId },
			success: function(response) {
				updateUpdateModelContent(response);
				$("#updateModel").modal("show");
			},
			error: function(xhr, status, error) {
				console.log("Error fetching candidate details:", error);
			}
		});
	});

	// Function to update the content of 'updateModel'
	function updateUpdateModelContent(candidateData) {

		var modalBody = $("#updateModel .modal-body");
		var genderDisplay = candidateData.gender === true ? "Female" : "Male";
		var submittedDateTime = candidateData.submitDate + " " + candidateData.submitTime;
		var submittedDateTime = candidateData.submitDate + " " + candidateData.submitTime;

		const submitDate = new Date(candidateData.submitDate);
		const day = String(submitDate.getDate()).padStart(2, '0');
		const month = String(submitDate.getMonth() + 1).padStart(2, '0');
		const year = submitDate.getFullYear(); // Change "date" to "submitDate"
		const formattedDate = `${day}-${month}-${year}`;

		const submitTime = String(candidateData.submitTime); // Convert to string
		const formattedTime = submitTime.replace(/,/g, ':');
		var submittedDateTime = formattedDate + " " + formattedTime;



		var content = `

      <div class="row">
        <div class="col-6">
          <b>Name </b>
        </div>
        <div class="col-6">
          <p>: ${candidateData.name}</p>
        </div>
        
        <div class="col-6">
          <b>Date of Birth </b>
        </div>
        <div class="col-6">
          <p>: ${candidateData.dob}</p>
        </div>
        
        <div class="col-6">
          <b>Gender </b>
        </div>
        <div class="col-6">
          <p>: ${genderDisplay}</p>
        </div>
        
        <div class="col-6">
          <b>Phone </b>
        </div>
        <div class="col-6">
          <p>: ${candidateData.phone}</p>
        </div>
        
        <div class="col-6">
          <b>Email </b>
        </div>
        <div class="col-6">
          <p>: ${candidateData.email}</p>
        </div>
        
        <div class="col-6">
          <b>Education </b>
        </div>
        <div class="col-6">
          <p>: ${candidateData.education}</p>
        </div>
        
        <div class="col-6">
          <b>Technical Skill </b>
        </div>
        <div class="col-6">
          <p>: ${candidateData.techSkill}</p>
        </div>
        
        <div class="col-6">
          <b>Language Skill </b>
        </div>
        <div class="col-6">
          <p>: ${candidateData.languageSkill}</p>
        </div>
        
        <div class="col-6">
          <b>Main Technology </b>
        </div>
        <div class="col-6">
          <p>: ${candidateData.mainTech}</p>
        </div>
        
        <div class="col-6">
          <b>Experience </b>
        </div>
        <div class="col-6">
          <p>: ${candidateData.exp}</p>
        </div>
        
        <div class="col-6">
          <b>Level </b>
        </div>
        <div class="col-6">
          <p>: ${candidateData.level}</p>
        </div>
        
        <div class="col-6">
          <b>Expected Salary </b>
        </div>
        <div class="col-6">
          <p>: ${candidateData.expectedSalary}</p>
        </div>
        
        <div class="col-6">
          <b>Submitted Date and Time </b>
        </div>
        <div class="col-6">
          <p>: ${submittedDateTime}</p>
        </div>
        
      </div>
      <div class="mt-3">
        <a href="/candidate/downloadCv?id=${candidateData.id}" class="btn btn-success btn-md" style="float:left">
        Download <i class="bi bi-download"></i>
    </a>
    <button type="button" class="btn btn-secondary"
                data-bs-dismiss="modal" style="float:right;">Close</button>
    </div>
    `
			;
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
		console.log(candidateId);
		// Determine the templateId based on selectedOption
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

						var emailPrepFormUrl = "/hr/email-prepare-form/" + templateId + "?type=" + selectedOption + "&candidateId=" + candidateId;

						$("#sendEmailBtn").on("click", function() {
							// Navigate to the email preparation form URL
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
		function showWarningModal(errorMessage) {
			$("#warningMessage").text(errorMessage);
			$('#warningModal').modal('show');
		}
	});

});
