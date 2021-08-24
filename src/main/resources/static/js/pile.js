editor = ace.edit("editor");
let ws;
let stompClient;
let subscriptionFor;

$(document).ready(function () {
    pileLib.initEditorText();
    wbConfig.initWebSocket();
    wbConfig.initStomp();
    if (($("#pile-owner").val() == "true") || ($("#pile-owner").val() == "false" && $("#pile-readonly").val() == "false")) {
        pileLib.initListeners();
        console.log("initing listeners")
        if ($("#pile-readonly").val() == "true") {
            wbConfig.connectToWebSocket();
        } else {
            wbConfig.connectToWebSocketWithSubscriptions();
        }
    } else {
        pileLib.makeReadOnlyAllFields();
        wbConfig.connectToWebSocketWithSubscriptions();
    }
});

let pileLib = {
    makeReadOnlyAllFields() {
        $("#pile-language").attr("disabled", true);
        editor.setReadOnly(true);
        $("#pile-tittle").prop("readonly", true);
    },

    initEditorText() {
        const editorText = $("#editor-text").val()
        editor.setValue(editorText);
    },

    initListeners() {
        $("#pile-language").change(function (event) {
            saveLanguageChange(this.value)
        })
        $("#pile-tittle").change(function (event) {
            saveTitleChange(this.value)
            sendTitleToWs(this.value)
        })

        $("#editor").change(function (event) {
            saveEditorTextChange()
        })
    }
}

function sendTitleToWs(title) {
    const subscribtion = $("#pile-subscirber").val();
    stompClient.send("/app/title/" + subscribtion, {}, JSON.stringify({'content': title}));
}

let wbConfig = {
    initWebSocket() {
        ws = new SockJS('/gs-guide-websocket')
    },

    initStomp() {
        stompClient = Stomp.over(ws);
    },
    connectToWebSocketWithSubscriptions() {
        const subscribtion = $("#pile-subscirber").val();
        stompClient.connect({}, function (frame) {
            stompClient.subscribe('/title/' + subscribtion, function (title) {
                pileTitleChange(JSON.parse(title.body));
            });
        });
    },

    connectToWebSocket() {
        stompClient.connect({}, function (frame) {
        });
    }
}

function pileTitleChange(title) {
    $("#pile-tittle").val(title.content)
}

function saveEditorTextChange() {
    const token = $('input[name="_csrf"]').val();

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

function saveTitleChange(title) {
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