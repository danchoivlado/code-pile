$(document).ready(function () {

    $(".mr-1").submit(function (event) {


        //stop submit the form, we will post it manually.
        event.preventDefault();
        fire_ajax_submit(this);

    });

});

function fire_ajax_submit(form){
    const changeToRole = $(this).attr('data-set-role');

}