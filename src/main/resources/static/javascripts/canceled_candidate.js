$(document).ready(function() {

	var baseAjaxUrl = '/senior-hr/canceled-candidate-data';

	function loadPositionsForSort(targetSelect) {

		$.ajax({
			url: "/hr/position-for-sort",
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
			url: "/hr/department-for-sort",
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

	loadPositionsForSort("#positionSort");
	loadDepartmentForSort("#departmentSort");


	function filterData(position, department) {
		var table = $('#candidateTable').DataTable();
		var positionFilter = position === 'all' ? '' : position;
		var departmentFilter = department === 'all' ? '' : department;
		table.search('').columns().search('').draw();

		table.column(1).search(positionFilter, true, false);
		table.column(2).search(departmentFilter, true, false);

		table.draw();
	}

	$('#positionSort,#departmentSort').on('change', function() {
		var selectedPosition = $('#positionSort').val();
		var selectedDepartment = $('#departmentSort').val();
		filterData(selectedPosition, selectedDepartment);
	});

	var table = $('#candidateTable').DataTable({
		processing: true,
		serverSide: true,
		ajax: {
			url: baseAjaxUrl,
			type: 'GET',
		},
		language: {
			"infoFiltered": ""
		},
		columns: [
			{ data: "name", responsivePriority: 1 },
			{ data: "vacancy.position.name", responsivePriority: 2 },
			{ data: "vacancy.department.name" },
			{ data: "mainTech", responsivePriority: 3 },
			{
				data: 'submitDate',
				responsivePriority: 4,
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
				data: "id",
				render: function(data, type, row) {
					var callBack = `<button type="button" class="btn btn-info call-back-btn" data-id="${row.id}" data-name="${row.name}" id="callBackBtn">Recall</button>`;

					return `<div class="btn-group">${callBack} </div>`;
				},
			},


			{
				data: "id",
				render: function(data, type, row) {
					var infoButton = `<button title="Candidate's Info" type="button" class="btn btn-info info-btn" data-id="${row.id}" id="infoButton"><i class="ti ti-info"></i></button>`;
					var historyButton = `<a title="Candidate's History" href="/hr/candidate-history/${row.id}" class="btn btn-primary"><i class="bi bi-clock-history text-white"></i></a>`;

					return `<div class="btn-group">${infoButton} ${historyButton}</div>`;
				},
				sortable: false, responsivePriority: 8
			},
		],
		order: [[4, 'asc']],
		paging: true,
		lengthMenu: [5, 10, 20],
		pageLength: 10,
		responsive: true,
	});

	$('#startDateFrom, #endDateTo,#interviewStageSelect').on('change', function() {
		var startDateFrom = $('#startDateFrom').val();
		var endDateTo = $('#endDateTo').val();
		var stage = $('#interviewStageSelect').val();
		table.ajax.url(baseAjaxUrl + '?startDateFrom=' + startDateFrom + '&endDateTo=' + endDateTo + '&stage=' + stage).load();

	});

	$("#resetButton").click(function() {

		$("#interviewStageSelect").val("All");

		$("#positionSort").val("all");
		$("#departmentSort").val("all");

		$("#startDateFrom").val("");
		$("#endDateTo").val("");
		table.clear().draw();
		table.columns().search('').draw();
		table.ajax.url(baseAjaxUrl).load();
	});


	var candidateIdForModal, candidateNameForModal;

	function generateRadioButtons(vacancies) {
		const modalBody = $('#modalBody');
		modalBody.find('input[name="vacancy"]').remove();
		modalBody.find('label.vacancy-label').remove();
		modalBody.find('label.vacancy-title').remove();
		modalBody.find('hr.vacancy-separator').remove();
		modalBody.find('label.no-vacancy-label').remove();
		if (modalBody.find('.vacancy-separator').length === 0) {
			const lineSeparator = $('<hr class="vacancy-separator">');
			modalBody.append(lineSeparator);
		}

		if (modalBody.find('.vacancy-title').length === 0) {
			const vacancyTitle = $('<label class="vacancy-title" style="font-family: sans-serif; font-weight: normal; display:block;">Vacancy</label>');
			modalBody.append(vacancyTitle);
		}

		if (vacancies === 0) {

			const noVacancyLabel = $('<br><label class="no-vacancy-label" style="color: red; font-family: sans-serif; font-weight: normal;">No active vacancies to recall </label>');
			modalBody.append(noVacancyLabel);
			$('#recallBtn').prop('disabled', true);
		} else {
			vacancies.forEach(vacancy => {
				var createdDate = new Date(vacancy.createdDate);
				var dueDate = new Date(vacancy.dueDate);
				var formattedCreatedDate = formatDate(createdDate);
				var formattedDueDate = formatDate(dueDate);
				const label = $('<label class="vacancy-label" style="font-family: sans-serif; font-weight: normal;"></label>');
				const input = $('<input type="radio" class="form-input" name="vacancy" value="' + vacancy.id + '">');
				label.append(input, vacancy.position.name + '(' + vacancy.department.name + ' - ' + formattedCreatedDate + '-' + formattedDueDate + ')', '<br>');
				modalBody.append(label);
			});
		}

	}

	function formatDate(date) {
		var year = date.getFullYear();
		var month = (date.getMonth() + 1).toString().padStart(2, '0'); // Month is zero-based
		var day = date.getDate().toString().padStart(2, '0');
		return day + '.' + month + '.' + year;
	}
	$(document).on('click', '.call-back-btn', function(e) {
		e.preventDefault();
		var button = $(this);
		candidateIdForModal = button.data('id');
		candidateNameForModal = button.data('name');
		$('#recallModal').modal('show');
	});
	$('#recallModal').on('shown.bs.modal', function() {
		console.log("hello");
		$.ajax({
			url: '/hr/get-active-vacancy',
			type: 'GET',
			success: function(vacancies) {
				console.log(vacancies.length);

				generateRadioButtons(vacancies);
			},
			error: function(error) {
				generateRadioButtons(0);
			}
		});
	});

	$(document).on('click', '#recallBtn', function(e) {
		e.preventDefault();
		$('#candidateNameSpan').text(candidateNameForModal);
		$('#candidateIdSpan').text(candidateIdForModal);

		var selectedType = $('input[name="recallType"]:checked').val();
		var selectedVacancy = $('input[name="vacancy"]:checked').val();

		var templateId = (selectedType === 'Online') ? '4' : '3';

		if (selectedType && selectedVacancy) {


			$.ajax({
				url: "/hr/choose-interview",
				type: "GET",
				data: {
					interviewType: selectedType,
					candidateId: candidateIdForModal,
					vacancyId: selectedVacancy,
					stage: 1
				},
				success: function(response) {
					$('#recallConfirmationModal').modal('show');
					$('#confirmRecallBtn').on('click', function() {
						var emailPrepFormUrl = "/senior-hr/email-prepare-form/" + templateId + "?type=" + selectedType + "&candidateId=" + candidateIdForModal + "&stage=1&vacancyId=" + selectedVacancy + "&isRecalled=1";
						window.location.href = emailPrepFormUrl;
					});
				},
				error: function(xhr) {
					$('#errorMessage').text(xhr.responseText);
					$('#errorModal').modal('show');
				}
			});
		}
		else {
			$('#errorMessageEmpty').show();
			setTimeout(function() {
				$('#errorMessageEmpty').hide();
			}, 2000);
		}


	});

	$("#errorModal").on('click', '#errorClose, #errorX', function() {
		$('#errorModal').modal('hide');
	});

	$("#recallModal").on('click', '#recallX, #RecallClose', function() {
		$('#recallModal').modal('hide');
	});

	$("#recallConfirmationModal").on('click', '#RecallConClose, #RecallConX', function() {
		$('#recallConfirmationModal').modal('hide');
	});


	//-------------------------------------------------


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
		console.log(candidateId);
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

						var emailPrepFormUrl = "/senior-hr/email-prepare-form/" + templateId + "?type=" + selectedOption + "&candidateId=" + candidateId + "&stage=" + stage;

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
