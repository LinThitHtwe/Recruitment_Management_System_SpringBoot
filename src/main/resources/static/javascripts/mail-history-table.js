$(document).ready(function() {
	var table = $('#candidateTable').DataTable({
		processing: true,
		serverSide: true,
		ajax: {
			url: '/hr/mail-history-data',
			type: 'GET',
		},
		columns: [
			 { 
                data: "id", // Use null as data source
                responsivePriority: 1,
                 
                render: function(data, type, row, meta) {
                    // Return the row number (meta.row + 1) as the content
                    return meta.row + 1;
                },
                sortable: false
             
            },
			{ data: "mailSubject", responsivePriority: 1 },
			{ data: "sentDateTime", responsivePriority: 2 },
			{ data: "candidate.email", responsivePriority: 3 },
			{ data: "user.name", responsivePriority: 4 },

		],
		order: [[0, 'desc']],
		paging: true,
		lengthMenu: [10, 15, 20], // Number of records per page
		pageLength: 10, // Default number of records per page

		responsive: true,
	});
});