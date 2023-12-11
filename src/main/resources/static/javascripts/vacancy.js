const responsibilities = document.getElementById('responsibilities');
const requirements = document.getElementById('requirements');
const preferences = document.getElementById('preferences');

//Function to move to the next line and add a bullet
function insertBulletOnEnter(event, textareaId) {
	if (event.key === 'Enter') {
		event.preventDefault();
		const bullet = '\u2022';
		const spaces = '      ';

		const textarea = document.getElementById(textareaId);
		const startPos = textarea.selectionStart;
		const endPos = textarea.selectionEnd;
		const content = textarea.value;

		const beforeText = content.substring(0, startPos);
		const afterText = content.substring(endPos);
		const newLine = bullet + spaces;

		textarea.value = beforeText + '\n' + newLine + afterText;

		const newCursorPos = startPos + newLine.length + 1;
		textarea.setSelectionRange(newCursorPos, newCursorPos);
		textarea.focus();
	}
}

//Add a bullet to current location
function addBullet(elementId) {
	const textarea = document.getElementById(elementId);
	const bullet = "\u2022 ";
	const spaces = "     "; // 5 spaces

	const startPos = textarea.selectionStart;
	const endPos = textarea.selectionEnd;
	const content = textarea.value;

	const newText = content.substring(0, startPos) + bullet + spaces + content.substring(endPos);
	textarea.value = newText;

	// Adjust the cursor position after inserting the bullet
	const newCursorPos = startPos + bullet.length + spaces.length;
	textarea.setSelectionRange(newCursorPos, newCursorPos);
	textarea.focus();
}

//Add bullets to selected text
function insertBulletAndSpaces(elementId) {
	const textarea = document.getElementById(elementId);
	const bullet = "\u2022 ";
	const spaces = "     "; // 5 spaces
	const content = textarea.value;
	var lines = content.split('\n');

	let hasBullets = true;

	for (const line of lines) {
		if (!line.startsWith(bullet)) {
			hasBullets = false;
			break;
		}
	}

	const modifiedLines = lines.map(function(line) {
		if (hasBullets) {
			// Remove bullets and spaces
			return line.slice(bullet.length + spaces.length);
		} else {
			// Add bullets and spaces
			return bullet + spaces + line;
		}
	});

	const newText = modifiedLines.join('\n');
	textarea.value = newText;
}

//Toggle the textarea size
function toggleTextareaSize(elementId, iconId) {
	const textarea = document.getElementById(elementId);
	const icon = document.getElementById(iconId);

	if (textarea.rows === 6) {
		const computed = window.getComputedStyle(textarea);
		const lineHeight = parseInt(computed.getPropertyValue('line-height'), 10);
		const maxHeight = lineHeight * 6;
		const scrollHeight = textarea.scrollHeight;

		textarea.rows = scrollHeight > maxHeight ? Math.ceil(scrollHeight / lineHeight) : 6;
		icon.className = scrollHeight > maxHeight ? 'bi bi-arrow-bar-up' : 'bi bi-arrow-bar-down';
	} else {
		textarea.rows = 6;
		icon.className = 'bi bi-arrow-bar-down';
	}
}

//Function to restrict the count of required vacancies
var requiredVacanciesInput = document.getElementById('required-vacancies');

requiredVacanciesInput.addEventListener('input', function() {
	var currentValue = parseInt(requiredVacanciesInput.value);

	if (!Number.isInteger(parseFloat(currentValue))) {
		requiredVacanciesInput.value = Math.floor(parseFloat(currentValue));
	}
});
// Change the default value of the start time
var startTimeInput = document.getElementById('start-working-time');
startTimeInput.value = '09:00';

// Change the default value of the end time
var endTimeInput = document.getElementById('end-working-time');
endTimeInput.value = '17:00';

//Change the default value of salary
var salaryInput = document.getElementById('salary');
salaryInput.value = 'Negotiable';

//Change the default value of location
var locationInput = document.getElementById('job-location');
locationInput.value = 'MICT Park, Haling Township, Yangon';

//Change the default value of job type
var typeInput = document.getElementById('job-type');
typeInput.value = 'Full Time';