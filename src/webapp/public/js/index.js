$(document).ready(function () {

    var x = 0;
    if(x===2){
        alert("x=2");
        window.location.href = "dashboard.html";
    }else{
        alert("x!=2");
    }

    $("#login_form").submit(function (event) {
        event.preventDefault();


        ajaxRequest("POST", "http://localhost:1234/login?username=" + $("#username").val() + "&password=" + $("#password").val()
            , function (data, textStatus, xhr) {
                if(xhr.status==200)
                    window.location.href="dashboard.html";
            }, function(data, textStatus, xhr){
                if(xhr.status==401) {
                    $("#login_failed").show();
                }else
                    console.log(xhr);
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