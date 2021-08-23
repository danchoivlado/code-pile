$(document).ready(function () {

    $("#check-button").click(function (event) {
        const username = $("#username").val();
        event.preventDefault();
        fire_ajax_click(username);
    });

});

function fire_ajax_click(username){
    const token =$('input[name="_csrf"]').val();

    let data = {};
    data['username']=username;

    $.ajax({
        type: "POST",
        beforeSend: function( xhr ) {
            xhr.setRequestHeader('X-CSRF-TOKEN', token);
        },
        contentType: "application/json",
        url: "/api/users/checkUserExistsWithUsername",
        data: JSON.stringify(data),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            let res = $("#check-response");
            if (data.msg === "exists"){
                res.html("Username is taken")
            }
            else {
                res.html("Username is free")
            }

        }
    });

}