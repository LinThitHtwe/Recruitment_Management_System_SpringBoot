$(document).ready(function() {
	var currentURL = window.location.href.split('?')[0];

	$('.nav-item').each(function() {
		var link = $(this).find('a');
		var targetURL = link.attr('th:href');
		targetURL = targetURL.split('?')[0];
		if (currentURL === targetURL) {
			$(this).addClass('active'); 
		}
	});
});





