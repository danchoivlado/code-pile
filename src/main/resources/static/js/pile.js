$(document).ready(function () {
    pileLib.initEditorText();
    pileLib.initListeners();
});
//  = null;
let pileLib = {
    initEditorText(){
        let editor = ace.edit("editor");
        const editorText = $("#editor-text").val()
        editor.setValue(editorText);
    },
    initListeners() {
        $("#pile-language").change(function (event) {
            saveLanguageChange(this.value)
        })

        $("#pile-tittle").change(function (event) {
            saveTitleChange(this.value)
        })

        $("#editor").change(function (event) {
            saveEditorTextChange()
        })
    }
}

function saveEditorTextChange(){
    const token = $('input[name="_csrf"]').val();
    let editor = ace.edit("editor");

    let data = {};
    data['pileId'] = $("#pile-id").val();
    data['editorText'] = editor.getValue();
    $.ajax({
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-TOKEN', token);
        },
        contentType: "application/json",
        url: "/api/pile/changeEditorText",
        data: JSON.stringify(data),
        dataType: 'json',
        cache: false,
        timeout: 600000
    });
}

function saveTitleChange(title){
    const token = $('input[name="_csrf"]').val();

    let data = {};
    data['pileId'] = $("#pile-id").val();
    data['title'] = title;

    $.ajax({
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-CSRF-TOKEN', token);
        },
        contentType: "application/json",
        url: "/api/pile/changeTitle",
        data: JSON.stringify(data),
        dataType: 'json',
        cache: false,
        timeout: 600000
    });
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
        timeout: 600000
    });
}