$(document).ready(function() {
	const urlParts = window.location.href.split('/');
	const vacancyId = urlParts[urlParts.length - 1];
	var savedPage = 0;
	var baseAjaxUrl = '/hr/candidate-data-by-vacancy/' + vacancyId;
	console.log(vacancyId);

	var table = $('#candidateTable').DataTable({
		processing: true,
		serverSide: true,
		ajax: {
			url: baseAjaxUrl,
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
			{ data: "exp" },
			{
				data: "candidateStatusList",
				render: function(data, type, row) {
					var lastStatus = data[data.length - 1];
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
				},
				sortable: false, responsivePriority: 6
			},
			{
				data: "candidateStatusList",
				render: function(data, type, row) {
					var lastStatus = data[data.length - 1];
					var statusOptions;
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
					if (currentUserRole === 'Senior-HR' || currentUserRole === 'Admin' || currentUserRole === 'Default-Admin') {
						if (lastStatus) {
							var statusName = lastStatus.status.name;
							var candidateId = row.id;

							if (statusName === "Received") {
								statusOptions = '<button class="btn btn-success status-btn btn-sm" data-candidate-id="' + candidateId + '" data-status-id="2" data-status-name="Viewed">Selection<i class="bi bi-arrow-up-circle" style="width:20px;"></i></button>';
							} else if (statusName === "Viewed") {
								statusOptions = '<button class="btn btn-success status-btn btn-sm" data-candidate-id="' + candidateId + '" data-status-id="3" data-status-name="Considering">Selection<i class="bi bi-arrow-up-circle" style="width:20px;"></i></button>';
							} else if (statusName === "Considering") {
								statusOptions = '<span style="text-align: center; display: inline-block; width: 100%;">-</span>';
							} else {
								statusOptions = '-';
							}
						}
						else {
							statusOptions = '-';
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
							statusClass = "";
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
					if (currentUserRole === 'Senior-HR' || currentUserRole === 'Admin' || currentUserRole === 'Default-Admin') {
						if (lastStatus && lastStatus.status && lastStatus.status !== "Passed" && lastStatus.status !== "Reached" && lastStatus.status !== "Cancel") {
							statusOptions = '<select class="form-control status-select status-select-class" data-candidate-id="' + candidateId + '" data-vacancy-id="' + vacancyId + '" data-candidate-name="' + candidateName + '">' +
								'<option value="" selected disabled>Select Interview Type</option>' +
								'<option value="Online">Online</option>' +
								'<option value="In-person">In-person</option>' +
								'</select>';
						} else if (lastStatus == null && lastSelectionStatus && lastSelectionStatus.status.name === "Considering") {
							statusOptions = '<select class="form-control status-select status-select-class" data-candidate-id="' + candidateId + '" data-vacancy-id="' + vacancyId + '" data-candidate-name="' + candidateName + '">' +
								'<option value="" selected disabled>Select Interview Type</option>' +
								'<option value="Online">Online</option>' +
								'<option value="In-person">In-person</option>' +
								'</select>';
						}
						else {
							if (isMailSent === "Sent" && isEmploy === "Employed") {
								statusOptions = '<div class="status-badge status-considering">Employed</div>';

							}

							else if (lastStatus && lastStatus.status === "Passed" && isMailSent === "UnSent") {
								statusOptions = '<div class="dash"><button title="Sent Job Offer Email" class="btn btn-success job-offer-btn" data-candidate-id="' + candidateId + '"><i class="bi bi-envelope-fill"></i></button></div>';
							} else if (lastStatus && lastStatus.status === "Passed" && isMailSent === "Sent") {
								statusOptions = '<div class="dash"><button title="Employ Candidate" class="btn btn-success employ-btn" data-candidate-id="' + candidateId + '" data-candidate-name="' + candidateName + '"><i class="bi bi-person-fill-add"></i></button></div>';
							}
							else {

								statusOptions = '<div class="dash">-</div>';
							}
						}
					} else {
						statusOptions = '<div class="dash">-</div>';
					}

					return statusOptions;
				},
				sortable: false,
				responsivePriority: 9
			},


			{
				data: "id",
				render: function(data, type, row) {
					var infoButton = `<button title="Candidate's Info" type="button" class="btn btn-info info-btn" data-id="${row.id}" id="infoButton"><i class="ti ti-info"></i></button>`;
					var historyButton = `<a title="Candidate's History" href="/hr/candidate-history/${row.id}" class="btn btn-primary"><i class="bi bi-clock-history text-white"></i></a>`;

					return `<div class="btn-group">${infoButton} ${historyButton}</div>`;
				},
				sortable: false, responsivePriority: 10
			},
		],
		order: [[5, 'desc']],
		paging: true,
		lengthMenu: [10, 15, 20],
		pageLength: 10,

		select: {
			style: "multi",
			selector: "td:first-child input[type=checkbox]:not(.info-btn input[type=checkbox])",
		},
		responsive: true,
	});


	$('#excelReportButton,#pdfReportButton').click(function() {
		var startDateFrom = $('#startDateFrom').val();
		var endDateTo = $('#endDateTo').val();
		var selectionStatus = $('#selectionStatusSelect').val();
		var interviewStatus = $('#interviewStatusSelect').val();
		var stage = $('#interviewStageSelect').val();
		var url = '';
		var type = '';


		if ($(this).attr('id') === 'excelReportButton') {
			type = 'excel';
		} else if ($(this).attr('id') === 'pdfReportButton') {
			type = 'pdf';
		}

		url = "/hr/download-candidate-summary/" + type
			+ "?startDateFrom=" + startDateFrom
			+ "&endDateTo=" + endDateTo + "&selectionStatus=" + selectionStatus + "&interviewStatus=" + interviewStatus + "&stage=" + stage + "&positionId=0&departmentId=0&vacancyId=" + vacancyId;


		$.ajax({
			url: url,
			method: "GET",
			success: function() {
				$('#messageContainer').html('<div class="alert alert-success alert-dismissible fade show" role="alert">' +
					'Download Successful! <button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
					'<span aria-hidden="true">&times;</span></button></div>');

				setTimeout(function() {
					$('#messageContainer').empty();
				}, 2000);
			},
			error: function(jqxhr) {
				$('#messageContainer').html('<div class="alert alert-danger alert-dismissible fade show" role="alert">' +
					'No data available for the selected criteria. <button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
					'<span aria-hidden="true">&times;</span></button></div>');

				setTimeout(function() {
					$('#messageContainer').empty();
				}, 2000);
			}
		});

		var iframe = $('<iframe/>', {
			style: 'display:none',
			src: url
		});
		$('body').append(iframe);

	});


	$('#selectionStatusSelect,#interviewStatusSelect,#interviewStageSelect').on('change', function() {
		var selectionStatus = $('#selectionStatusSelect').val();
		var interviewStatus = $('#interviewStatusSelect').val();
		var stage = $('#interviewStageSelect').val();
		table.ajax.url(baseAjaxUrl + '?selectionStatus=' + selectionStatus + '&interviewStatus=' + interviewStatus + '&stage=' + stage).load();

	});

	$("#resetButton").click(function() {

	
		$("#selectionStatusSelect").val("All");
	$("#interviewStatusSelect").val("All");
		$("#interviewStageSelect").val("All");

		table.clear().draw();
		table.columns().search('').draw();
		table.ajax.url(baseAjaxUrl).load();
	});

	function updateCandidateStatus(candidateId, newStatusId) {
		$.ajax({
			url: '/hr/changeSelectionStatus',
			type: 'GET',
			data: { candidateId: candidateId, statusId: newStatusId },
			success: function() {
				$('#successAlert').show();

				setTimeout(function() {
					$('#successAlert').hide();
				}, 3000);

				table.ajax.reload();
			},
			error: function() {
				alert('Failed to update candidate status.');

				table.ajax.reload();
			}
		});
	}

	$(document).on('click', '.employ-btn', function() {
		var candidateName = $(this).data('candidate-name');
		$('#candidateNameEmploy').text(candidateName);
		$('#employConfirmationModal').modal('show');

		var candidateId = $(this).data('candidate-id');
		var candidateName = $(this).data('candidate-name');

		$('#confirmEmploy').click(function() {
			employCandidate(candidateId);
		});
	});

	function employCandidate(candidateId) {

		$.ajax({
			type: 'POST',
			url: '/hr/employ-candidate',
			data: { candidateId: candidateId },
			success: function(response) {

				$('#successAlert').show();

				setTimeout(function() {
					$('#successAlert').hide();
				}, 3000);

				table.ajax.reload();

			},
			error: function(xhr, status, error) {

				console.error('Error while employing candidate:', error);
			}
		});


		$('#employConfirmationModal').modal('hide');
	}


	$(document).on('click', '.job-offer-btn', function(e) {
		e.preventDefault();
		var type = ""
		var stage = 0
		var button = $(this);
		var candidateId = button.data('candidate-id');
		var url = "/senior-hr/email-prepare-form/5" + "?type=" + type + "&candidateId=" + candidateId + "&stage=" + stage;

		window.location.href = url;
	});

	$(document).on('click', '.status-btn', function(e) {
		var button = $(this);
		var candidateId = button.data('candidate-id');
		var newStatusId = button.data('status-id');
		var newStatusName = button.data('status-name');

		$('#statusName').text(newStatusName);

		$('#confirmationModal').modal('show');

		$('#confirmStatusChange').data('candidate-id', candidateId);
		$('#confirmStatusChange').data('status-id', newStatusId);

		e.stopPropagation();
	});


	$(document).on('click', '#confirmStatusChange', function(e) {

		$('#confirmationModal').modal('hide');

		var candidateId = $(this).data('candidate-id');
		var newStatusId = $(this).data('status-id');

		updateCandidateStatus(candidateId, newStatusId);

		e.stopPropagation();
	}); confirmCloseX


	$(document).on('click', '#cancelStatusChange', function() {
		$('#confirmationModal').modal('hide');
		$('.status-select').val('');
	});

	$(document).on('click', '#confirmCloseX', function() {
		$('#confirmationModal').modal('hide');
		$('.status-select').val('');
	});

	$('#confirmationModal').on('hidden.bs.modal', function(e) {
		$('.status-select').val('');
	});
	$('#warningModal').on('click', '.close', function() {
		$('.status-select').val('');
	});

	$('#warningModal').on('click', '#closeBtn', function() {
		$('#warningModal').modal('hide');
		$('.status-select').val('');
	});

	$('#warningModal').on('hidden.bs.modal', function() {
		$('.status-select').val('');
	});

	$('#successModal').on('click', '.close', function() {
		$('#successModal').modal('hide');
		savedPage = table.page();
		table.ajax.reload(function() {
			table.page(savedPage).draw('page');
		});
	});

	$('#successModal').on('click', '#closeBtn', function() {
		$('#successModal').modal('hide');
		$('.status-select').val('');
	});

	$('#successModal').on('hidden.bs.modal', function() {
		$('.status-select').val('');
	});


	$('#employConfirmationModal').on('click', '.close', function() {
		$('#employConfirmationModal').modal('hide');
	});

	$('#employConfirmationModal').on('click', '#cancelEmployChange', function() {
		$('#employConfirmationModal').modal('hide');
		$('.status-select').val('');
	});

	$('#employConfirmationModal').on('hidden.bs.modal', function() {
		$('.status-select').val('');
	})
	const deselectButton = document.getElementById("deselectButton");

	deselectButton.addEventListener("click", function() {
		const selectElements = document.querySelectorAll("select");
		selectElements.forEach(function(selectElement) {
			selectElement.selectedIndex = 0;
		});

		$(".candidate-checkbox").prop("checked", false);

		table.rows().deselect();
	});

	$("#candidateTable tbody").on("click", "td:first-child", function() {
		var row = $(this).closest("tr");
		var checkbox = row.find(".ca`ndidate-checkbox");
		var isSelected = checkbox.prop("checked");
		checkbox.prop("checked", !isSelected);
		table.row(row).select(!isSelected);
		updateCheckboxState();
	});

	// Event listener for DataTable selection change
	table.on("select deselect", function() {
		updateCheckboxState();
	});
	updateCheckboxState();
	// Function to update checkbox state based on DataTable selection
	function updateCheckboxState() {
		var selectedRows = table.rows({ selected: true }).nodes();
		var checkboxes = $(".candidate-checkbox", selectedRows);
		var deselectButton = $("#deselectButton");

		if (checkboxes.length > 0) {
			deselectButton.show();

		} else {
			deselectButton.hide();

		}

		var allCheckboxes = $(".candidate-checkbox", table.rows().nodes());
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
			$('#errorAlert').show();

			setTimeout(function() {
				$('#errorAlert').hide();
			}, 3000);
		}
	});

	$("#candidateTable tbody").on("click", ".info-btn", function() {
		var rowId = $(this).data("id");
		$.ajax({
			url: "/hr/getCandidateInfo",
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

	function hideWarningModal() {
		$('#warningModal').modal('hide');
	}


	$(document).on('change', '.status-select-class', function() {
		var candidateId = $(this).data('candidate-id');
		var candidateName = $(this).data('candidate-name');
		var vacancyId = $(this).data('vacancy-id');
		var selectedOption = $(this).val();
		var templateId = (selectedOption === 'Online') ? '4' : '3';

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

						var emailPrepFormUrl = "/senior-hr/email-prepare-form/" + templateId + "?type=" + selectedOption + "&candidateId=" + candidateId + "&stage=" + stage + "&vacancyId=" + vacancyId;

						$("#sendEmailBtn").on("click", function() {
							window.location.href = emailPrepFormUrl;
						});

					},
					error: function(error) {
						console.log(error.responseText);
						var errorMessage = error.responseText;
						showWarningModal(errorMessage);
					}
				});
			},
			error: function(error) {
				console.log(error.responseText);
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
