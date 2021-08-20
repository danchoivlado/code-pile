var stompClient = null;

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sub(){
    stompClient.subscribe('/topic/titleRecieve', function (title) {
        changeTitle(JSON.parse(title.body).content);
    });
}

function changeTitle(titleContent){
    $( "#pile-tittle").val(titleContent);
}

function sendTitle(){
    console.log(JSON.stringify({'content': $("#title").val()}))
    stompClient.send("/app/titleSend",{}, JSON.stringify({'content': $("#pile-tittle").val()}));
}

$(function () {
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#sub" ).click(function() { sub(); });
    $( "#pile-tittle" ).change(function() { sendTitle(); });
});