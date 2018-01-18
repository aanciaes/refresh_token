$(document).ready(function () {

    var x = 2;
    if(x===2){
        alert("x=2");
        window.location.href = "test.html";
    }else{
        alert("x!=2");
    }

    $("#login_form").submit(function (event) {
        event.preventDefault();


        ajaxRequest("POST", "http://localhost:1234/login?username=" + $("#username").val() + "&password=" + $("#password").val()
            , function (data) {
                alert(data)
                window.location.href="test.html";
            }, true);

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