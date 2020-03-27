function tableDocumentReady() {
    $('#orders').DataTable({
        "order": [[3, "desc"]],
        "scrollX": true,
        "columnDefs": [
            { "orderable": false, "targets": 7 }
        ]
    });
    $('#orders_wrapper').find('label').each(function () {
        $(this).parent().append($(this).children());
    });
    $('#orders_wrapper .dataTables_filter').find('input').each(function () {
        const $this = $(this);
        $this.attr("placeholder", "Search");
        $this.removeClass('form-control-sm');
    });
    $('#orders_wrapper .dataTables_length').addClass('d-flex flex-row');
    $('#orders_wrapper .dataTables_filter').addClass('md-form');
    $('#orders_wrapper select').removeClass(
        'custom-select custom-select-sm form-control form-control-sm');
    $('#orders_wrapper select').addClass('mdb-select');
    $('#orders_wrapper .mdb-select').materialSelect();
    $('#orders_wrapper .dataTables_filter').find('label').remove();
}
