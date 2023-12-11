//for tinymce texteditor
tinymce.init({
	selector: '#body',
	plugins: 'anchor autolink charmap codesample emoticons image link lists media searchreplace table visualblocks wordcount checklist mediaembed casechange export formatpainter pageembed linkchecker a11ychecker tinymcespellchecker permanentpen powerpaste advtable advcode editimage tinycomments tableofcontents footnotes mergetags autocorrect typography inlinecss',
	toolbar: 'undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | link image media table mergetags | addcomment showcomments | spellcheckdialog a11ycheck typography | align lineheight | checklist numlist bullist indent outdent | emoticons charmap | removeformat',
	tinycomments_mode: 'embedded',
	tinycomments_author: 'Author name',
	mergetags_list: [
		{ value: 'First.Name', title: 'First Name' },
		{ value: 'Email', title: 'Email' },
	]
});


//show confirmation box
function showConfirmationBox() {
 var editor = tinymce.get('body');
 var bodyContent = editor.getContent();
 var plainText = bodyContent.replace(/<[^>]*>/g, '');

    if (plainText.trim() == '') {
        $('#emailBodyEmptyModal').modal('show');
    }
 else{
  $('#mailConfirmationModal').modal('show');
 }
 
}


//send mail with ajax
function sendMail() {
	var editor = tinymce.get('body');
	var bodyContent = editor.getContent();

	// Update the textarea with the TinyMCE content
	$('#body').val(bodyContent);
		
	$('#mailSendErrorModal').modal('hide');
	$('#mailConfirmationModal').modal('hide');
	$('#mailLoadingModal').modal('show');
//	var vacancyIdValue = $('#vacancyId').val();
//	var interviewId = $('#interviewId').val();
	$.ajax({
		type: 'POST',
		url: '/senior-hr/send-customized-mail',
		data: $("#sendMailForm").serialize(),
		success: function(response) {
			if (response == "success") {
				setTimeout(function() {
					$('#mailLoadingModal').modal('hide');
					$('#sendMailSuccessModal').modal('show');
				}, 500);
			} else {
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