let editor = ace.edit("editor");


$(document).ready(function () {
    editorLib.init();

    $("#pile-language").change(function (event) {
        changeLanguage(this.value)
    })

});

function changeLanguage(selectedMode) {
    editorLib.setMode(selectedMode)
}


let editorLib = {
    init() {
        const selectedMode = $("#pile-language").children("option:selected").val();
        console.log(selectedMode)
        editor.setTheme("ace/theme/crimson_editor");
        editor.getSession().setMode("ace/mode/" + selectedMode);
        editor.setOptions({
            fontSize: '12pt',
            enableBasicAutocompletion: true,
            enableLiveAutocompletion: true,
        });
    },
    setMode(mode) {
        editor.getSession().setMode("ace/mode/" + mode);
    }
}



