let stompClient = null;
connect();

function connect() {
    let socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        switchToElement(elements.JOIN_GAME);
        stompClient.subscribe('/user/join/error', function (data) {
            data = JSON.parse(data.body)
            if (data["status"] === "failed") {
                $('#joinErrorModal').modal('show')
                $('#joinErrorModalText').text(data["message"])
            } else if (data["status"] === "success") {
                switchToElement(elements.LOADER);
            }
        });
    });
}

function joinGame() {
    stompClient.send("/app/join", {}, JSON.stringify({'quiz_pin': $("#quiz_pin").val(), 'team_name': $("#team_name").val()}));
}

const loaderElement = $("#loader");
const joinElement = $("#join");

function switchToElement(element) {
    loaderElement.css("display", "none");
    joinElement.css("display", "none");

    if (element === elements.JOIN_GAME) {
        joinElement.css("display", "inline");
    } else {
        loaderElement.css("display", "inline");
    }
}

const elements = {
    LOADER: 1,
    JOIN_GAME: 2,
};

function generateRandomString(length) {
    let result = '';
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let charactersLength = characters.length;
    for (let i = 0; i < length; i++) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
}
