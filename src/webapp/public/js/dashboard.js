$(document).ready(function () {

    $("#user_details").click(function () {
        alert("clicked");
        ajaxRequest("POST", "http://localhost:1234/verify", function (data) {
            alert(data)
        }, true);
    });

});

function ajaxRequest(type, url, success, credentials) {
    var params = {
        type: type,
        url: url,
        crossDomain: true,
        success: success,

        error: function () {
            alert("ERRO");
        }
    };

    if (credentials)
        params.xhrFields = {withCredentials: true};

    $.ajax(params);
}