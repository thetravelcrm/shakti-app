function alertSuccess(msg) {
    $.alert({
        title: 'Alert!',
        content: msg,
        type: 'green',
        autoClose: 'OK|5000',
        typeAnimated: true,
        buttons: {
            OK: function () {

            }
        }
    });

}

function alertWarning(msg) {
    $.alert({
        title: 'Alert!',
        content: msg,
        type: 'red',
        autoClose: 'OK|5000',
        typeAnimated: true,
        buttons: {
            OK: function () {

            }
        }
    });

}
function alertWithTitle(title,msg) {
    $.alert({
        title: title,
        content: msg,
        type: 'red',
        autoClose: 'OK|5000',
        typeAnimated: true,
        buttons: {
            OK: function () {

            }
        }
    });

}
function ConfirmDialog(msg, func) {
    $.confirm({
        title: 'Confirm!',
        content: msg + '!',
        autoClose: 'No|10000',
        buttons: {
            Yes: function () {
                func();

            },
            No: function () {

            },

        }
    });
}