$(document).ready(function() {
  $('.email-button').on('click', function() {
    var emailBodyContent = $(this).closest('tr').find('label').text();
    $('#bodyPlaceholder').html(emailBodyContent);
    $('#emailBodyModal').modal('show');
  });
});
