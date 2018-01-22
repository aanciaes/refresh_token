$(document).ready(function () {

    ajaxRequest("GET", "http://localhost:1234/user", null,  function (data) {
        buildUserDetails(data);
    }, function (xhr, textStatus) {
        if(xhr.status===401){
            window.location.href = "index.html";
        }
        if(xhr.status===403){
            window.location.href = "index.html";
        }
    }, true);

    $("#admin").click(adminOperation);

    $("#logout_btn").click(function () {
        ajaxRequest("POST", "http://localhost:1234/logout", null,
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

function adminOperation(){
    ajaxRequest("GET", "http://localhost:1234/refresh?resource=ADMIN_RESOURCE", null,
        function (data) {
            var obj = JSON.parse(data);
            getAdminResource(obj.accessToken, obj.type);
        },
        function (xhr) {
            console.log(xhr);
            window.location.href="index.html"
        }, true);
}

function getAdminResource (accessToken, type){
    var headers = {
        "Authorization" : type + " " + accessToken
    };
    ajaxRequest("GET", "http://localhost:1234/admin", headers,
        function (data) {
            alert(data);
        },
        function (xhr) {
            alert(xhr);
        }, true)
}

//Generic ajax request function
function ajaxRequest(type, url, headers, success, error, credentials) {
    var params = {
        type: type,
        url: url,
        crossDomain: true,
        success: success,
        error: error
    };

    if (credentials)
        params.xhrFields = {withCredentials: true};
    if(headers!=null)
        params.headers = headers;

    $.ajax(params);
}