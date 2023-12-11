$(document).ready(function() {
	var dataTable = $('#positionTable').DataTable({
		processing: true,
		serverSide: true,
		ajax: {
			url: '/hr/positions-data',
			type: 'GET',
		},
		columns: [
			{ data: 'name', name: 'Position' },
			{ data: 'createdDateTime', name: 'Created DateTime' },
			{data: 'updatedDateTime',name: 'Updated DateTime',
   			render: function(data) {
        	return data !== null ? data : '-';}},

			{
				data: "id",
				render: function(data, type, row) {
					var infoButton = `<button title="Position's Info" type="button" class="btn btn-info info-btn" data-id="${row.id}" id="infoButton"><i class="ti ti-info"></i></button>`;
					var editButton = `<button title="Edit Position" type="button" class="btn btn-primary edit-btn" data-id="${row.id}"><i class="ti ti-pencil"></i></button>`;
					return `
						<div class="btn-group">
							${infoButton}
							${editButton}
						</div>
					`;
				},
				sortable: false
			},
		],
		order: [[0, 'asc']],
		paging: true,
		lengthMenu: [5, 10, 20],
		pageLength: 5,
	});

	$('#positionTable').on('click', 'button.info-btn', function() {
		var positionId = $(this).data('id');

		$.ajax({
			type: 'GET',
			url: '/hr/position-detail',
			data: {
				id: positionId
			},
			success: function(response) {
    		$('#detailModal .modal-body').html(
        	'<h4>Position Name: ' + response.name + '</h4>' +
        	'<p>Created By: ' + response.createdUsername + '</p>' +
        	'<p>Created DateTime: ' + response.createdDateTime + '</p>' +
        	'<p>Updated By: ' + (response.updatedUsername !== null ? response.updatedUsername : '-') + '</p>' +
        	'<p>Updated DateTime: ' + (response.updatedDateTime !== null ? response.updatedDateTime : '-') + '</p>'
    			);

    		$('#detailModal').modal('show');
},

			error: function(xhr) {
				console.error(xhr.responseText);
			}
		});
	});

	$('#positionTable').on('click', '.edit-btn', function() {
		var positionId = $(this).data('id');
		var positionName = $(this).closest('tr').find('td:first-child').text();

		$('#positionUpdateModalLabel').text('Position - Update');
		$('#positionUpdateName').val(positionName);
		$('#positionId').val(positionId);

		$('#positionUpdateModal').modal('show');
	});

	$('#positionUpdateModal').on('hidden.bs.modal', function() {
		$('#positionUpdateModalLabel').text('Position - Update');
		$('#positionUpdateName').val('');
		$('#error-msg-update').text('').hide();
		$('#updatePositionForm button[type="submit"]').prop('disabled', false);
	});
	


	// function for adding new position
	$("#addPositionForm").submit(function(event) {
		event.preventDefault(); // Prevent default form submission

		var form = $(this);
		var url = form.attr("action");

		$.ajax({

			type: "POST",
			url: url,
			data: form.serialize(),
			success: function(response) {	
				$("#addPositionForm")[0].reset();		
				$('#positionModal').modal('hide');
				dataTable.ajax.reload();
				$('#successAlert').show();

				// Hide the alert after 3 seconds (3000 milliseconds)
				setTimeout(function() {
					$('#successAlert').hide();
				}, 3000);
			}

			,
			error: function(xhr) {
				var errorMessage = xhr.responseText;
				displayErrorMessage(errorMessage, true);
			}
		});
	});



	$("#updatePositionForm").submit(function(event) {
		event.preventDefault();

		var form = $(this);
		var url = form.attr("action");
		var positionId = $('#positionId').val();
		url = url.replace('{id}', positionId);

		$.ajax({
			type: "POST",
			url: url,
			data: form.serialize(),
			success: function(response) {

				$('#positionUpdateModal').modal('hide');
				dataTable.ajax.reload();
				$('#successAlert').show();

				// Hide the alert after 3 seconds (3000 milliseconds)
				setTimeout(function() {
					$('#successAlert').hide();
				}, 3000);

			
			},
			error: function(xhr) {
				var errorMessage = "An error occurred while updating the position. Please try again later.";
				displayErrorMessageUpdate(errorMessage, true);
			}
		});
	});

});