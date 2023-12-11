//add or remove cc fields
function addCCField() {
	const ccContainer = document.getElementById('ccContainer');

	// Create a new CC field
	const ccField = document.createElement('div');
	ccField.classList.add('cc-field');

	const input = document.createElement('input');
	input.setAttribute('placeholder', 'Enter cc mail');
	input.setAttribute('type', 'email');
	input.setAttribute('name', 'ccAddress');
	input.setAttribute('id', 'ccAddress');
	input.setAttribute('class', 'formbold-form-input');

	const minusButton = document.createElement('i');
	minusButton.classList.add('bi', 'bi-x-square-fill', 'minus-button');

	ccField.appendChild(input);
	ccField.appendChild(minusButton);

	// Append the new ccField element to the container
	ccContainer.appendChild(ccField);
}

// Attach event delegation for the "Remove" button
document.addEventListener('click', function(event) {
	if (event.target.classList.contains('bi') &&
		event.target.classList.contains('bi-x-square-fill') &&
		event.target.classList.contains('minus-button')) {
		const ccField = event.target.parentNode;
		ccField.parentNode.removeChild(ccField);
	}
});

const datePicker = document.getElementById('appointmentDate');
const today = new Date().toISOString().split('T')[0];
datePicker.setAttribute('min', today);

const joinedStartDateMin = document.getElementById('joinedStartDate');
joinedStartDateMin.setAttribute('min', today);


//add or remove attachment fields
function addAttachmentField() {
	const attachmentContainer = document.getElementById('attachmentContainer');

	// Create a new CC field
	const attachmentField = document.createElement('div');
	attachmentField.classList.add('attachment-field');

	const input = document.createElement('input');
	input.setAttribute('placeholder', 'e.g.C:\Users\example.docx');
	input.setAttribute('type', 'text');
	input.setAttribute('name', 'attachment');
	input.setAttribute('id', 'attachment');
	input.setAttribute('class', 'formbold-form-input');

	const minusButton = document.createElement('i');
	minusButton.classList.add('bi', 'bi-x-square-fill', 'attachment-minus-button');

	attachmentField.appendChild(input);
	attachmentField.appendChild(minusButton);

	// Append the new ccField element to the container
	attachmentContainer.appendChild(attachmentField);
}

// Attach event delegation for the "Remove" button
document.addEventListener('click', function(event) {
	if (event.target.classList.contains('bi') &&
		event.target.classList.contains('bi-x-square-fill') &&
		event.target.classList.contains('attachment-minus-button')) {
		const attachmentField = event.target.parentNode;
		attachmentField.parentNode.removeChild(attachmentField);
	}
});

document.getElementById('appointmentDate').addEventListener('change', function() {

	const selectedDate = new Date(this.value);
	const daysOfWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
	const selectedDay = daysOfWeek[selectedDate.getDay()];
	console.log(selectedDay);

	const appointmentDaySelect = document.getElementById('appointmentDay');
	for (let i = 0; i < appointmentDaySelect.options.length; i++) {
		if (appointmentDaySelect.options[i].value === selectedDay) {
			appointmentDaySelect.selectedIndex = i;
			break;
		}
	}
});


//validate form fields start
function validateFormFields() {
	var templateId = document.getElementById('templateId');
	var subject = document.getElementById("subject");
	var appointmentDate = document.getElementById('appointmentDate');
	var appointmentDay = document.getElementById('appointmentDay');
	var appointmentStartTime = document.getElementById('appointmentStartTime');
	var appointmentEndTime = document.getElementById('appointmentEndTime');
	var zoomMeetingUrl = document.getElementById('zoomMeetingUrl');
	var zoomMeetingId = document.getElementById('zoomMeetingId');
	var zoomMeetingPasscode = document.getElementById('zoomMeetingPasscode');
	var joinedStartDate = document.getElementById('joinedStartDate');
	var basicPay = document.getElementById('basicPay');
	var projectAllowance = document.getElementById('projectAllowance');
	var mealTransportAllowanceDay = document.getElementById('mealTransportAllowanceDay');
	var mealTransportAllowanceMonth = document.getElementById('mealTransportAllowanceMonth');
	var earnLeave = document.getElementById('earnLeave');
	var casualLeave = document.getElementById('casualLeave');
	var medicalLeave = document.getElementById('medicalLeave');
	var endTimeEarlierThanStartTime = false;
	var invalidTime = false;
	// Reset error messages
	resetErrorMessages();

	if (appointmentStartTime.value.trim() !== "" && appointmentEndTime.value.trim() !== "") {
		var startTime = new Date(`2000-01-01T${appointmentStartTime.value}`);
		var endTime = new Date(`2000-01-01T${appointmentEndTime.value}`);
		endTimeEarlierThanStartTime = endTime <= startTime;
		console.log(startTime);
		console.log(endTime);
		var invalidTime = false;

		if ((startTime < new Date(`2000-01-01T08:00`) || startTime >= new Date(`2000-01-01T19:00`)) ||
			(endTime < new Date(`2000-01-01T08:00`) || endTime >= new Date(`2000-01-01T19:00`))) {
			console.log("invalidTime");
			invalidTime = true;
		}
	}



	//for in-person interview
	if (templateId.value === "3") {
		if (subject.value.trim() === "" || /^\s+$/.test(subject.value) || appointmentDate.value.trim() === "" || appointmentDay.value.trim() === "" || appointmentStartTime.value.trim() === "" || appointmentEndTime.value.trim() === "" || endTimeEarlierThanStartTime) {

			// Validate subject
			if (subject.value.trim() === "" || /^\s+$/.test(subject.value)) {
				displayErrorMessage(subject, "Subject can't be blank.");
			}

			// Validate appointment date
			if (appointmentDate.value.trim() === "") {
				displayErrorMessage(appointmentDate, "Appointment Date can't be blank.");
			}

			// Validate appointment day
			if (appointmentDay.value.trim() === "") {
				displayErrorMessage(appointmentDay, "Appointment Day can't be blank.");
			}

			// Validate appointment start time
			if (appointmentStartTime.value.trim() === "") {
				displayErrorMessage(appointmentStartTime, "Appointment Start Time can't be blank.");
			}

			// Validate appointment end time
			if (appointmentEndTime.value.trim() === "") {
				displayErrorMessage(appointmentEndTime, "Appointment End Time can't be blank.");
			}

			if (endTimeEarlierThanStartTime) {
				displayErrorMessage(appointmentEndTime, "End time must be later than start time.");
			}

			if (invalidTime) {
				console.log("invalidTime Error");
				displayErrorMessage(appointmentEndTime, "Time Should be between 8am and 7 pm");
			}


			return false;
		}
	}

	//for online interview
	if (templateId.value === "4") {
		if (subject.value.trim() === "" || /^\s+$/.test(subject.value) || appointmentDate.value.trim() === "" || appointmentDay.value.trim() === "" || appointmentStartTime.value.trim() === "" || appointmentEndTime.value.trim() === ""
			|| zoomMeetingUrl.value.trim() === "" || /^\s+$/.test(zoomMeetingUrl.value) || zoomMeetingId.value.trim() === "" || /^\s+$/.test(zoomMeetingId.value) || zoomMeetingPasscode.value.trim() === "" || /^\s+$/.test(zoomMeetingPasscode.value) || endTimeEarlierThanStartTime) {

			// Validate subject
			if (subject.value.trim() === "" || /^\s+$/.test(subject.value)) {
				displayErrorMessage(subject, "Subject can't be blank.");
			}

			// Validate appointment date
			if (appointmentDate.value.trim() === "") {
				displayErrorMessage(appointmentDate, "Appointment Date can't be blank.");
			}

			// Validate appointment day
			if (appointmentDay.value.trim() === "") {
				displayErrorMessage(appointmentDay, "Appointment Day can't be blank.");
			}

			// Validate appointment start time
			if (appointmentStartTime.value.trim() === "") {
				displayErrorMessage(appointmentStartTime, "Appointment Start Time can't be blank.");
			}

			// Validate appointment end time
			if (appointmentEndTime.value.trim() === "") {
				displayErrorMessage(appointmentEndTime, "Appointment End Time can't be blank.");
			}

			// Validate zoom url
			if (zoomMeetingUrl.value.trim() === "" || /^\s+$/.test(zoomMeetingUrl.value)) {
				displayErrorMessage(zoomMeetingUrl, "Zoom Meeting Url can't be blank.");
			}

			// Validate zoom id
			if (zoomMeetingId.value.trim() === "" || /^\s+$/.test(zoomMeetingId.value)) {
				displayErrorMessage(zoomMeetingId, "Zoom Meeting ID can't be blank.");
			}

			// Validate zoom passcode
			if (zoomMeetingPasscode.value.trim() === "" || /^\s+$/.test(zoomMeetingPasscode.value)) {
				displayErrorMessage(zoomMeetingPasscode, "Zoom Meeting Passcode can't be blank.");
			}

			//end time must be later
			if (endTimeEarlierThanStartTime) {
				displayErrorMessage(appointmentEndTime, "End time must be later than start time.");
			}

			if (invalidTime) {
				displayErrorMessage(appointmentEndTime, "Time Should be between 8am and 7 pm");
			}

			return false;
		}
	}

	//for job offer
	if (templateId.value === "5") {
		if (subject.value.trim() === "" || /^\s+$/.test(subject.value) || joinedStartDate.value.trim() === "" || basicPay.value.trim() === "" || projectAllowance.value.trim() === "" || mealTransportAllowanceDay.value.trim() === ""
			|| mealTransportAllowanceMonth.value.trim() === "" || earnLeave.value.trim() === "" || casualLeave.value.trim() === "" || medicalLeave.value.trim() === "") {

			// Validate subject
			if (subject.value.trim() === "" || /^\s+$/.test(subject.value)) {
				displayErrorMessage(subject, "Subject can't be blank.");
			}


			// Validate joined start date
			if (joinedStartDate.value.trim() === "") {
				displayErrorMessage(joinedStartDate, "joined Start Date can't be blank.");
			}

			// Validate basic pay
			if (basicPay.value.trim() === "") {
				displayErrorMessage(basicPay, "Basic Pay can't be blank.");
			}

			// Validate project allowance
			if (projectAllowance.value.trim() === "") {
				displayErrorMessage(projectAllowance, "Project Allowance can't be blank.");
			}

			// Validate meal transport allowanceDay
			if (mealTransportAllowanceDay.value.trim() === "") {
				displayErrorMessage(mealTransportAllowanceDay, "Meal Transport Allowance Per Day can't be blank.");
			}

			// Validate meal transport allowance month
			if (mealTransportAllowanceMonth.value.trim() === "") {
				displayErrorMessage(mealTransportAllowanceMonth, "Meal Transport Allowance Month can't be blank.");
			}

			// Validate earn leave
			if (earnLeave.value.trim() === "") {
				displayErrorMessage(earnLeave, "Earn Leave can't be blank.");
			}

			// Validate casual leave
			if (casualLeave.value.trim() === "") {
				displayErrorMessage(casualLeave, "Casual Leave can't be blank.");
			}

			// Validate medical leave
			if (medicalLeave.value.trim() === "") {
				displayErrorMessage(medicalLeave, "Medical Leave can't be blank.");
			}



			return false;
		}
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
//validate form fields end

//show confirmation box
function showConfirmationBox() {
	$('#mailConfirmationModal').modal('show');
}

function validateAndShowConfirmationBox(event) {
	if (validateFormFields()) {
		showConfirmationBox();
	} else {
		// Prevent form submission if there are validation errors
		event.preventDefault();
	}
}


//show email draft
function showDraft() {

	// Send the AJAX request
	$.ajax({
		url: "/senior-hr/show-emailbody-draft", // Your server-side endpoint
		type: "POST",
		data: $("#emailPrepareForm").serialize(),
		success: function(response) {
			$('#bodyPlaceholder').html(response);
			$('#emailDraftModal').modal('show');
		},
		error: function(xhr, status, error) {
			// Handle errors if any
			console.error(error);
		},
	});
}


//send mail
function sendMail() {
	$('#mailSendErrorModal').modal('hide');
	$('#mailConfirmationModal').modal('hide');
	$('#mailLoadingModal').modal('show');
	$.ajax({
		type: 'POST',
		url: '/senior-hr/send-mail',
		data: $("#emailPrepareForm").serialize(),
		success: function(response) {
			if (response == "success") {
				setTimeout(function() {
					$('#mailLoadingModal').modal('hide');
					$('#sendMailSuccessModal').modal('show');
				}, 500);
			} else if (response.includes("Invalid attachments") || response.includes("Invalid CC addresses")) {
				setTimeout(function() {
					$('#mailLoadingModal').modal('hide');
					$('#invalidEmailAttachmentPlaceholder').text(response);
					$('#invalidAttachmentEmailModal').modal('show');
				}, 1000);
			}

			else {
				setTimeout(function() {
					$('#mailLoadingModal').modal('hide');
					$('#errorPlaceholder').text(response);
					$('#mailSendErrorModal').modal('show');
				}, 1000);
			}
		},
		error: function(xhr, status, error) {
			console.log(xhr.responseText);
		}
	});
}

//validate cc and attachments before going to customize page
function validateCCAndAttachment() {


	$.ajax({
		type: 'POST',
		url: '/senior-hr/validate-cc-attachment',
		data: $("#emailPrepareForm").serialize(),
		success: function(response) {
			if (response == "success") {
				customizeEmailbody();
			}
			else {
				setTimeout(function() {
					$('#invalidEmailAttachmentPlaceholder').text(response);
					$('#invalidAttachmentEmailModal').modal('show');
				}, 1000);
			}

		},
		error: function(xhr, status, error) {
			console.log(xhr.responseText);
		}
	});
}


//send to customize page
function customizeEmailbody() {

	//if all cc are hidden
	const ccFields = document.querySelectorAll('.cc-field');
	const attachmentFields = document.querySelectorAll('.attachment-field');
	ccFields.forEach(ccField => {
		const isCCHidden = ccField.style.display === 'none';

		if (!isCCHidden) {
			const ccAddressInput = ccField.querySelector('input[name="ccAddress"]');
			const ccAddress = ccAddressInput.value;

		}
	});

	attachmentFields.forEach(attachmentField => {
		const isAttachmentHidden = attachmentField.style.display === 'none';

		if (!isAttachmentHidden) {
			const attachmentInput = attachmentField.querySelector('input[name="attachment"]');

		}
	});


	const templateId = document.getElementById('templateId').value;
	const candidate_id = document.getElementById('candidate_id').value;
	const interview_id = document.getElementById('interview_id').value;
	const recipientAddress = document.getElementById('recipientAddress').value;
	const subject = document.getElementById('subject').value;
	//const ccAddress = document.getElementById('ccAddress').value;
	//const attachment = document.getElementById('attachment').value;
	const appointmentDate = document.getElementById('appointmentDate').value;
	const appointmentDay = document.getElementById('appointmentDay').value;
	const appointmentStartTime = document.getElementById('appointmentStartTime').value;
	const appointmentEndTime = document.getElementById('appointmentEndTime').value;
	const zoomMeetingUrl = document.getElementById('zoomMeetingUrl').value;
	const zoomMeetingId = document.getElementById('zoomMeetingId').value;
	const zoomMeetingPasscode = document.getElementById('zoomMeetingPasscode').value;
	const joinedStartDate = document.getElementById('joinedStartDate').value;
	const basicPay = document.getElementById('basicPay').value;
	const projectAllowance = document.getElementById('projectAllowance').value;
	const mealTransportAllowanceDay = document.getElementById('mealTransportAllowanceDay').value;
	const mealTransportAllowanceMonth = document.getElementById('mealTransportAllowanceMonth').value;
	const earnLeave = document.getElementById('earnLeave').value;
	const casualLeave = document.getElementById('casualLeave').value;
	const medicalLeave = document.getElementById('medicalLeave').value;
	const vacancyId = document.getElementById('vacancyId').value;
	const interviewId = document.getElementById('interview_id').value;
	const isRecalled = document.getElementById('isRecalled').value;
	console.log(isRecalled);

	const form = document.createElement('form');
	// Set the form attributes
	form.method = 'GET';
	form.action = '/senior-hr/customize-email/' + candidate_id + '?vacancyId=' + vacancyId + '&interviewId=' + interviewId + '&isRecalled=' + isRecalled;

	function addHiddenField(name, value) {
		const input = document.createElement('input');
		input.type = 'hidden';
		input.name = name;
		input.value = value;
		form.appendChild(input);
	}
	//if all cc are hidden
	ccFields.forEach(ccField => {
		const isCCHidden = ccField.style.display === 'none';

		if (!isCCHidden) {
			const ccAddressInput = ccField.querySelector('input[name="ccAddress"]');
			addHiddenField('ccAddress', ccAddressInput.value);


		}
	});

	//if all attachment are hidden
	attachmentFields.forEach(attachmentField => {
		const isAttachmentHidden = attachmentField.style.display === 'none';

		if (!isAttachmentHidden) {
			const attachmentInput = attachmentField.querySelector('input[name="attachment"]');
			const attachment = document.getElementById('attachment').value;
			addHiddenField('attachment', attachmentInput.value);

		}
	});

	addHiddenField('templateId', templateId);
	addHiddenField('isRecalled', isRecalled);
	addHiddenField('candidate_id', candidate_id);
	addHiddenField('interview_id', interview_id);
	addHiddenField('recipientAddress', recipientAddress);
	addHiddenField('subject', subject);
	//addHiddenField('attachment', attachment);
	addHiddenField('appointmentDate', appointmentDate);
	addHiddenField('appointmentDay', appointmentDay);
	addHiddenField('appointmentStartTime', appointmentStartTime);
	addHiddenField('appointmentEndTime', appointmentEndTime);
	addHiddenField('zoomMeetingUrl', zoomMeetingUrl);
	addHiddenField('zoomMeetingId', zoomMeetingId);
	addHiddenField('zoomMeetingPasscode', zoomMeetingPasscode);
	addHiddenField('joinedStartDate', joinedStartDate);
	addHiddenField('basicPay', basicPay);
	addHiddenField('projectAllowance', projectAllowance);
	addHiddenField('mealTransportAllowanceDay', mealTransportAllowanceDay);
	addHiddenField('mealTransportAllowanceMonth', mealTransportAllowanceMonth);
	addHiddenField('earnLeave', earnLeave);
	addHiddenField('casualLeave', casualLeave);
	addHiddenField('medicalLeave', medicalLeave);

	// Append the form to the document body and submit it
	document.body.appendChild(form);
	form.submit();

}