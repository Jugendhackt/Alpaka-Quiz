let stompClient = null;
connect();

function connect() {
    let socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/join/error', function (data) {
            data = JSON.parse(data.body)
            if (data["status"] === "failed") {
                $('#joinErrorModal').modal('show')
                $('#joinErrorModalText').text(data["message"])
            }
            console.log(data)
        });
    });
}

function joinGame() {
    stompClient.send("/app/join", {}, JSON.stringify({'quiz_pin': $("#quiz_pin").val(), 'team_name': $("#team_name").val()}));
}
