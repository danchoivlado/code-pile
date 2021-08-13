
$(document).ready(function () {

    $("#bth-search").click(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();

        fire_ajax_submit();

    });

});

function fire_ajax_submit() {

    const token =$('input[name="_csrf"]').val();
    console.log(token)

    $.ajax({
        type: "POST",
        beforeSend: function( xhr ) {
            xhr.setRequestHeader('X-CSRF-TOKEN', token);
        },
        contentType: "application/json",
        url: "/api/test",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $("#bth-search").html('sss');

        }
    });

}
