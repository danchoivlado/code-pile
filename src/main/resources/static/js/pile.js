$(document).ready(function () {
    pileLib.initListeners()
});

let pileLib = {
    initListeners() {
        $("#pile-language").change(function (event) {
            saveLanguageChange(this.value)
        })
    }
}

function saveLanguageChange(selectedLanguage) {
    const token = $('input[name="_csrf"]').val();

    let data = {};
    data['pileId'] = $("#pile-id").val();
    data['language'] = selectedLanguage

    $.ajax({
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-TOKEN', token);
        },
        contentType: "application/json",
        url: "/api/pile/changeLanguage",
        data: JSON.stringify(data),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log("huq mi qnko")

        }
    });
}