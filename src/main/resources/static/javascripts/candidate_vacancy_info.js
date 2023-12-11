$(document).ready(function() {
		var successAlert = $("#successAlert");
	if (successAlert.length) {
		setTimeout(function() {
			successAlert.alert('close'); 
		}, 2000);
	}
	
	const form = $("#myForm");
	const inputFields = form.find(".form-control");
	const urlParts = window.location.href.split('/');
	const lastPartWithQuery = urlParts[urlParts.length - 1];
	const lastPart = lastPartWithQuery.split('?')[0]; 

const vacancyId = lastPart;
	let isEmailValid = true;

	inputFields.each(function() {
		$(this).blur(function() {
			validateField($(this));

			if ($(this).attr("id") === "email") {
				validateDuplicate($(this));
			}
		});
	});

	function validateField(input) {
		const value = input.val().trim();
		const validationMessage = input.next(".invalid-feedback");

		if (value === "") {
			input.removeClass("is-valid");
			input.addClass("is-invalid");
			validationMessage.text("This field is required.");
		} else {
			if (input.attr("id") === "name") {
				if (!/^[A-Z][A-Za-z\s]*$/.test(value)) {
					input.removeClass("is-valid");
					input.addClass("is-invalid");
					validationMessage.text("Name should start with a capital letter and can include letters and spaces.");
				} else {
					input.removeClass("is-invalid");
					input.addClass("is-valid");
					validationMessage.text("");
				}
			} else if (input.attr("id") === "dob") {
				const dobDate = new Date(value);
        const currentDate = new Date();
        const minAge = 18;
        const age = new Date(currentDate.getFullYear() - minAge, currentDate.getMonth(), currentDate.getDate());

        if (dobDate >= age) {
          input.removeClass("is-valid");
          input.addClass("is-invalid");
          validationMessage.text("You should be at least 18 years old.");
        } else {
          input.removeClass("is-invalid");
          input.addClass("is-valid");
          validationMessage.text("");
        }

			} else if (input.attr("id") === "phone") {
			if (value.length == 9 && /^09\d{7}$/.test(value) || value.length == 11 && /^09\d{9}$/.test(value)) {
          
          input.removeClass("is-invalid");
          input.addClass("is-valid");
          validationMessage.text("");
          
        } else {
          input.removeClass("is-valid");
          input.addClass("is-invalid");
          validationMessage.text("Please enter the valid phone number that start with 09.");
        }

			} else if (input.attr("id") === "email") {

				validateDuplicate(input);

			} else if (input.attr("id") === "levelSelect") {
				const selectedValue = input.val();
				if (selectedValue === "Please Choose Level") {
					input.removeClass("is-valid");
					input.addClass("is-invalid");
					validationMessage.text("Please select your level.");
				} else {
					input.removeClass("is-invalid");
					input.addClass("is-valid");
					validationMessage.text("");
				}
			} else if (input.attr("id") === "techSkill") {
				if (value.trim() === "") {
					input.removeClass("is-valid");
					input.addClass("is-invalid");
					validationMessage.text("Technical Skill cannot be empty.");
				} else {
					input.removeClass("is-invalid");
					input.addClass("is-valid");
					validationMessage.text("");
				}
			}

			else if (input.attr("id") === "education") {
		
				if (value.trim() === "") {
					input.removeClass("is-valid");
					input.addClass("is-invalid");
					validationMessage.text("Technical Skill cannot be empty.");
				} else {
					input.removeClass("is-invalid");
					input.addClass("is-valid");
					validationMessage.text("");
				}
			} else if (input.attr("id") === "exp") {
			
				if (value.trim() === "") {
					input.removeClass("is-valid");
					input.addClass("is-invalid");
					validationMessage.text("Experience cannot be empty.");
				} else {
					input.removeClass("is-invalid");
					input.addClass("is-valid");
					validationMessage.text("");
				}

			} else if (input.attr("id") === "languageSkill") {
			
				if (value.trim() === "") {
					input.removeClass("is-valid");
					input.addClass("is-invalid");
					validationMessage.text("Language Skill cannot be empty.");
				} else {
					input.removeClass("is-invalid");
					input.addClass("is-valid");
					validationMessage.text("");
				}
			}
			else if (input.attr("id") === "mainTech") {
				
				if (value.trim() === "") {
					input.removeClass("is-valid");
					input.addClass("is-invalid");
					validationMessage.text("Main Tech cannot be empty.");
				} else {
					input.removeClass("is-invalid");
					input.addClass("is-valid");
					validationMessage.text("");
				}
			} else if (input.attr("id") === "expectedSalary") {
				
				if (value.trim() === "") {
					input.removeClass("is-valid");
					input.addClass("is-invalid");
					validationMessage.text("Expected Salarycannot be empty.");
				} else {
					input.removeClass("is-invalid");
					input.addClass("is-valid");
					validationMessage.text("");
				}
			} else if (input.attr("id") === "file") {
				const fileInput = input[0];
				const file = fileInput.files[0];
				const fileValidationMessage = $("#fileValidationMessage");

				if (!file) {
					input.removeClass("is-valid");
					input.addClass("is-invalid");
					fileValidationMessage.text("Please select your CV file.");
				} else if (!isValidFileFormat(file.name)) {
					input.removeClass("is-valid");
					input.addClass("is-invalid");
					fileValidationMessage.text("Invalid file format. Please select a Word or PDF file.");
				} else {
					input.removeClass("is-invalid");
					input.addClass("is-valid");
					fileValidationMessage.text("");
				}
			}

			// ...

			function isValidFileFormat(fileName) {
				const validExtensions = ["docx", "pdf"];
				const fileExtension = fileName.split(".").pop().toLowerCase();
				return validExtensions.includes(fileExtension);
			}


		}

	}

	function isValidEmailFormat(email) {
		const emailPattern = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
		return emailPattern.test(email);
	}

	function validateDuplicate(emailInput) {
		const emailValue = emailInput.val().trim();
		const emailValidationMessage = $("#emailValidationMessage");
		let emailFormat
		if (emailValue !== "") {
			$.ajax({
				url: "/candidate/check-candidate-duplication",
				data: {
					email: emailValue,
					vacancyId: vacancyId
				},
				type: "GET",
				success: function(response) {
					emailInput.removeClass("is-invalid");
					emailInput.addClass("is-valid");
					emailValidationMessage.text("");
					isEmailValid = true;
				},
				error: function(xhr) {
					const errorMessage = xhr.responseText;
					emailInput.removeClass("is-valid");
					emailInput.addClass("is-invalid");
					emailValidationMessage.text(errorMessage);
					isEmailValid = false;
				}
			});
		}

	}

	form.submit(function(event) {
		let isValid = true;

		inputFields.each(function() {
			validateField($(this));

			if ($(this).hasClass("is-invalid")) {
				isValid = false;
			}
		});

		validateDuplicate($("#email"));

		if (!isValid || !isEmailValid) {
			event.preventDefault();
		}
	});
});
