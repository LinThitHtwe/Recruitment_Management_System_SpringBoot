$(document).ready(function() {
	var confirmed = false;

	var currentRole = $("#currentUserRole").text();

	function loadUsers(page, rowCount) {
		$('#userTable').DataTable().ajax.reload();
	}

	$('#filterSelect, #roleFilterSelect').on('change', function() {
		var selectedRole = $('#roleFilterSelect').val();
		var selectedStatus = $('#filterSelect').val();
		filterData(selectedRole, selectedStatus);
	});

	function filterData(role, status) {
		var table = $('#userTable').DataTable();
		var roleFilter = role === 'All' ? '' : role;
		var statusFilter = status === 'all' ? '' : status;

		table.column(2).search(roleFilter);
		table.column(4).search(statusFilter);

		table.draw();
	}

	var table = $('#userTable').DataTable({
		processing: true,
		serverSide: true,
		ajax: {
			url: '/department-head/user-data',
			type: 'GET',
		},
		columns: [

			{ data: 'name', name: 'Name' },
			{ data: 'email', name: 'Email' },
			{ data: 'role', name: 'Role' },
			{
				data: 'department.name',
				name: 'Department',
				render: function(data, type, row) {
					return data ? data : '<div class="dash">-</div>';;
				}
			},
			{
				data: "status",
				render: function(data, type, row) {
					var statusText = data ? "Inactive" : "Active";
					var statusClass = data ? "status-decline" : "status-considering";

					return '<div class="status-badge ' + statusClass + '">' + statusText + '</div>';
				},
				sortable: false
			},



		],
		order: [[0, 'asc']],
		paging: true,
		lengthMenu: [5, 10, 20],
		pageLength: 5,
		language: {
			"infoFiltered": ""
		}
	});

});
