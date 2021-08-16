$(document).ready(function () {

    $(".mr-1").submit(function (event) {

        const changeToRole = $(this).attr('data-set-role');
        const userId = this.getElementsByClassName('user-id')[0].value;
        event.preventDefault();
        fire_ajax_submit(changeToRole, userId);

    });

});

function fire_ajax_submit(changeToRole, userId){
    const token =$('input[name="_csrf"]').val();

    let data = {};
    data['toRole']=changeToRole;
    data['id'] = userId;

    $.ajax({
        type: "POST",
        beforeSend: function( xhr ) {
            xhr.setRequestHeader('X-CSRF-TOKEN', token);
        },
        contentType: "application/json",
        url: "/api/users/set-user",
        data: JSON.stringify(data),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {


        }
    });

}