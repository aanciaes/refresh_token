$(document).ready(function () {

    ajaxRequest("POST", "http://localhost:1234/verify",
        function(data, textStatus, xhr){
            if(xhr.status==200)
                window.location.href="dashboard.html";
        },
        function () {}, true);

    $("#login_form").submit(function (event) {
        event.preventDefault();

        ajaxRequest("POST", "http://localhost:1234/login?username=" + $("#username").val() + "&password=" + $("#password").val()
            , function (data, textStatus, xhr) {
                if(xhr.status==200)
                    window.location.href="dashboard.html";
            }, function(xhr,textStatus,err){
                if(xhr.status===401) {
                    $("#login_failed").show();
                }else{
                    console.log(xhr);
                    $("#login_failed").show();
                }
            }, true);
    });
});

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