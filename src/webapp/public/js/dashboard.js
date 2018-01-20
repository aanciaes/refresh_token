$(document).ready(function () {

    ajaxRequest("GET", "http://localhost:1234/user", function (data) {
        alert(data);
    }, function (xhr, textStatus) {
        if(xhr.status===401){
            window.location.href = "index.html";
        }
    }, true);


    $("#user_details").click(function () {
        ajaxRequest("POST", "http://localhost:1234/verify", function (data) {
            alert(data)
        },
        function (xhr,textStatus,err) {
            console.log(xhr)
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