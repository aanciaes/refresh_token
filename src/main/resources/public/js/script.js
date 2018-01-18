$(document).ready(function () {
    $("#login_form").submit(function (event) {
        event.preventDefault();

        $.ajax({
            type: "POST",
            url: "/login?username=" + $("#username").val() + "&password=" + $("#password").val(),
            crossDomain: true,
            data: {
                username: $("#username").val(),
                password: $("#password").val()
            },
            success: function (data) {
                alert(data);
                document.cookie = "refresh_token_fake=qwerty_refreshToken; path=/";
            }
        });
    });

    $("#second_request").click(function () {
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