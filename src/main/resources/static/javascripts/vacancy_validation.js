document.addEventListener("DOMContentLoaded", function() {

	const form = document.querySelector(".forms-sample");
	const requiredVacancies = document.getElementById("required-vacancies");
	const requiredError = document.getElementById("countError");
	const description = document.getElementById("description");
	const descriptionError = document.getElementById("descriptionError");
	const responsibilities = document.getElementById("responsibilities");
	const responsibilitiesError = document.getElementById("responsibilitiesError");
	const requirements = document.getElementById("requirements");
	const requirementError = document.getElementById("requirementError");
	const preferences = document.getElementById("preferences");
	const preferencesError = document.getElementById("preferencesError");
	const salary = document.getElementById("salary");
	const salaryError = document.getElementById("salaryError");
	const position = document.getElementById("position");
	const positionError = document.getElementById("positionError");
	const department = document.getElementById("department");
	const departmentError = document.getElementById("departmentError");
	const startTimeInput = document.getElementById("start-working-time");
	const endTimeInput = document.getElementById("end-working-time");
	const timeError = document.getElementById("time-error");

	form.addEventListener("submit", function(event) {
		if (requiredVacancies.value === "" || parseFloat(requiredVacancies.value) <= 0) {
			event.preventDefault();
			requiredError.style.display = "inline-block";
			window.scrollTo(0, 0);
		}
		validateCondition(event, description, descriptionError);
		validateCondition(event, responsibilities, responsibilitiesError);
		validateCondition(event, requirements, requirementError);
		validateCondition(event, preferences, preferencesError);
		validateCondition(event, salary, salaryError);
		validateCondition(event, position, positionError);
		validateCondition(event, department, departmentError);
		validateTime(event, startTimeInput, endTimeInput, timeError)
	});

	setTimeOut(requiredVacancies, requiredError);
	setTimeOut(description, descriptionError);	setTimeOut(responsibilities, responsibilitiesError);	setTimeOut(requirements, requirementError);	setTimeOut(preferences, preferencesError);	setTimeOut(salary, salaryError);
	setTimeOut(startTimeInput, timeError);
	setTimeOut(endTimeInput, timeError);
	console.log(startTimeInput.value);
	console.log(endTimeInput.value);
});

function validateCondition(event, element, errorMsg) {
	if (element.value.trim() === "") {
		event.preventDefault();
		errorMsg.style.display = "inline-block";
		window.scrollTo(0, 0);
	}
}

function setTimeOut(element, errorMsg) {
	element.oninput = function() {
		errorMsg.style.display = "none";
	}
}

function validateTime(event, startTimeInput, endTimeInput, errorMsg) {
	const startTime = startTimeInput.value.split(":");
	const endTime = endTimeInput.value.split(":");
	const startHour = parseInt(startTime[0]);
	const endHour = parseInt(endTime[0]);

	if (startHour > endHour) {
		event.preventDefault();
		errorMsg.style.display = "inline-block";
		window,scrollTo(0,0);
	}
}