editor = ace.edit("editor");
let ws;
let stompClient;
let subscriptionFor;

$(document).ready(function () {
    run();
});

function run() {
    pileLib.initEditorText();
    wbConfig.initWebSocket();
    wbConfig.initStomp();
    pileLib.configureSubscribersAndListeners()
}

function isReadOnlyEnabled() {
    return $("#pile-readonly").val() == "true";
}

function isCurrentUserOwner() {
    return $("#pile-owner").val() == "true";
}

let pileLib = {

    configureSubscribersAndListeners() {
        if (!isCurrentUserOwner()) {
            pileLib.makeReadOnlyAccessModeFields()
        }
        if ((isCurrentUserOwner()) || (!isCurrentUserOwner() && !isReadOnlyEnabled())) {
            pileLib.initListeners();
            if (isReadOnlyEnabled()) {
                wbConfig.connectToWebSocket();
            } else {
                wbConfig.connectToWebSocketWithSubscriptions();
            }
        } else {
            pileLib.makeReadOnlyAllFields();
            wbConfig.connectToWebSocketWithSubscriptions();
        }
    },

    makeReadOnlyAccessModeFields() {
        $('input[type=radio][name=accessRadioButton]').attr('disabled', true);
    },

        makeReadOnlyAllFields() {
        $("#pile-language").attr("disabled", true);
        editor.setReadOnly(true);
        $("#pile-tittle").prop("readonly", true);
    },

    makeAccessibleAllField() {
        $("#pile-language").attr("disabled", false);
        editor.setReadOnly(false);
        $("#pile-tittle").prop("readonly", false);
    },

    initEditorText() {
        const editorText = $("#editor-text").val()
        editor.setValue(editorText);
    },

    initListeners() {
        $('input[type=radio][name=accessRadioButton]').change(function () {
            changeMode(this.value)
        })

        $("#pile-language").change(function (event) {
            sendLanguageToWs(this.value)
        })
        $("#pile-tittle").change(function (event) {
            sendTitleToWs(this.value)
        })

        $("#editor").change(function (event) {
            sendEditorTextChange()
        })
    },

    unbindListeners() {
        $('input[type=radio][name=accessRadioButton]').unbind()
        $("#pile-language").unbind()
        $("#pile-tittle").unbind()
        $("#editor").unbind()
    }
}

function changeMode(mode) {
    const subscribtion = $("#pile-subscirber").val();
    let accessMode = {};
    accessMode["pileId"] = $("#pile-id").val();
    accessMode["readOnly"] = mode === "readOnly" ? true : false;
    stompClient.send("/app/accessMode/" + subscribtion, {}, JSON.stringify(accessMode));
}

function sendTitleToWs(title) {
    const subscribtion = $("#pile-subscirber").val();
    let titleObj = {};
    titleObj["pileId"] = $("#pile-id").val();
    titleObj["content"] = title;
    stompClient.send("/app/title/" + subscribtion, {}, JSON.stringify(titleObj));
}

let wbConfig = {

    unsubscribeAll() {
        for (const sub in stompClient.subscriptions) {
            stompClient.unsubscribe(sub)
        }
    },

    disconnectClient() {
        stompClient.disconnect(function (s) {
            console.log('disconnect')
        });
    },

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
            stompClient.subscribe('/language/' + subscribtion, function (title) {
                pileLanguageChange(JSON.parse(title.body));
            });
            stompClient.subscribe('/accessMode/' + subscribtion, function (title) {
                accessModeChange(JSON.parse(title.body));
            });
            stompClient.subscribe('/editorText/' + subscribtion, function (title) {
                editorTextChange(JSON.parse(title.body));
            });
        });
    },

    connectToWebSocket() {
        const subscribtion = $("#pile-subscirber").val();
        stompClient.connect({}, function (frame) {
            stompClient.subscribe('/accessMode/' + subscribtion, function (title) {
                accessModeChange(JSON.parse(title.body));
            });
        });
    }
}

function editorTextChange(editorText){
    editor.setValue(editorText.content);
    $("#editor-text").val(editorText.content)
}

function accessModeChange(accessMode) {
    console.log(accessMode)
    changeDependantInputs(accessMode)
    pileLib.unbindListeners();
    changeRadioButton(accessMode);
    changeFields(accessMode)
    wbConfig.unsubscribeAll()
    wbConfig.disconnectClient()
    run()
}

function changeFields(accessMode) {
    if (!accessMode.readOnly) {
        pileLib.makeAccessibleAllField();
    }
}

function changeDependantInputs(accessMode) {
    $("#pile-readonly").val(accessMode.readOnly)
    $("#pile-subscirber").val(accessMode.subscription)
}

function changeRadioButton(accessMode) {

    if (accessMode.readOnly) {
        $("#readOnlyRadioButton").prop("checked", true);
    } else {
        $("#partyOnRadioButton").prop("checked", true);
    }
}


function pileTitleChange(title) {
    $("#pile-tittle").val(title.content)
}

function pileLanguageChange(language) {
    $("#pile-language").val(language.content)
    editorLib.setMode(language.content)
}

function sendEditorTextChange(){
    $("#editor-text").val(editor.getValue())
    const subscribtion = $("#pile-subscirber").val();
    let editorObj = {};
    editorObj["pileId"] = $("#pile-id").val();
    editorObj["content"] = editor.getValue();
    stompClient.send("/app/editorText/" + subscribtion, {}, JSON.stringify(editorObj));
}

function sendLanguageToWs(selectedLanguage){
    const subscribtion = $("#pile-subscirber").val();
    let languageObj = {};
    languageObj["pileId"] = $("#pile-id").val();
    languageObj["content"] = selectedLanguage;
    stompClient.send("/app/language/" + subscribtion, {}, JSON.stringify(languageObj));
}