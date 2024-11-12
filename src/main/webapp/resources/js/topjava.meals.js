const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});
function applyFilter() {
    const startDate = $('#startDate').val();
    const endDate = $('#endDate').val();
    const startTime = $('#startTime').val();
    const endTime = $('#endTime').val();

    $.get(ctx.ajaxUrl + "filter", {
        startDate: startDate || null,
        endDate: endDate || null,
        startTime: startTime || null,
        endTime: endTime || null
    }, function (data) {
        populateTable(data);
    });
}
function clearFilter() {
    document.getElementById('filter').reset();
    updateTable();
}