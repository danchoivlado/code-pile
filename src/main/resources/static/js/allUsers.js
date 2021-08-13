$(document).ready(function () {

    $(".mr-1").submit(function (event) {

        const changeToRole = $(this).attr('data-set-role');
        //stop submit the form, we will post it manually.
        event.preventDefault();
        fire_ajax_submit(changeToRole);

    });

});

function fire_ajax_submit(changeToRole){

    const userId = $("#userId").val();
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