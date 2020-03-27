function navBarInit() {
    if ($(location).attr("href").indexOf('mine') >= 0) {
        $('#my-orders').attr('class', 'nav-item active');
    } else if ($(location).attr("href").indexOf('me') >= 0) {
        $('#me').attr('class', 'nav-item active');
    } else if ($(location).attr("href").indexOf('queued') >= 0) {
        $('#new-orders').attr('class', 'nav-item active');
    } else if ($(location).attr("href").indexOf('all') >= 0) {
        $('#all-orders').attr('class', 'nav-item active');
    }
}
