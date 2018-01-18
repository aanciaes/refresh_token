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
            , function (data) {
                alert(data)
                window.location.href="dashboard.html";
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