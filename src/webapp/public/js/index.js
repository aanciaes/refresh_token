$(document).ready(function () {

    /**
     * Verify is there is already a refresh token cookie in the browser.
     * If there is a token and it is valid, then an user is already authenticated
     * and redirects to dashboard page
     */
    ajaxRequest("POST", "http://localhost:1234/verify",
        function (data, textStatus, xhr) {
            if (xhr.status == 200)
                window.location.href = "dashboard.html";
        },
        function () {
        }, true);
    //

    $("#login_form").submit(function (event) {
        event.preventDefault();

        ajaxRequest("POST", "http://localhost:1234/login?username=" + $("#username").val() + "&password=" + $("#password").val()
            , function (data, textStatus, xhr) {
                if (xhr.status == 200)
                    window.location.href = "dashboard.html";

            }, function (xhr, textStatus, err) {

                if (xhr.status === 401) {
                    $("#login_failed").show();
                    $("#username").val('');
                    $("#password").val('');
                } else {
                    console.log(xhr);
                    $("#login_failed").show();
                    $("#username").val('');
                    $("#password").val('');
                }
            }, true);
    });
});

//Generic ajax request function
function ajaxRequest(type, url, success, error, credentials) {
    var params = {
        type: type,
        url: url,
        crossDomain: true,
        success: success,
        error: error
    };

    if (credentials)
        params.xhrFields = {withCredentials: true};

    $.ajax(params);
}