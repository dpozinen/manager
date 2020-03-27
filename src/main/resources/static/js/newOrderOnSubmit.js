function newOrderOnSubmit() {
    $('#new-order-form').on('submit', function (e) {
        var order = {};

        order['notes'] = $('#new-order-notes').val();
        order['payState'] = $('#new-order-payed').val();
        order['price'] = $('#new-order-price').val();
        order['client'] = $('#new-order-client').val();

        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        var xhr = new XMLHttpRequest();

        var url = "/order/create";

        xhr.onload = function () {
            if (this.status != 200) {
                alert('Could not process that request, please try again');
                location.reload();
            }
        };

        xhr.open("POST", url, true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.setRequestHeader(header, token);

        xhr.send(JSON.stringify(order));
    });
}
