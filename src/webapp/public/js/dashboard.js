$(document).ready(function () {

    ajaxRequest("GET", "http://localhost:1234/user", function (data) {
        buildUserDetails(data);
    }, function (xhr, textStatus) {
        if(xhr.status===401){
            window.location.href = "index.html";
        }
        if(xhr.status===403){
            alert("Session has expired");
            window.location.href = "index.html";
        }
    }, true);

    $("#logout_btn").click(function () {
        ajaxRequest("POST", "http://localhost:1234/logout",
            function (data) {
                window.location.href="index.html";
            }, function (xhr) {
                console.log(xhr);
            }, true);
    });

});

function buildUserDetails (httpResponse) {
    var user = JSON.parse(httpResponse);

    $("#username").append("Username: " + user.username);
    $("#isAdmin").append("Is administrator: " + user.admin);
}

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