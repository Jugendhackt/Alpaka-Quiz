let stompClient = null;

function connect(callback) {
    let socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        callback();
    });
}

const loaderElement = $("#loader");


const BOOTSTRAP_COLORS = ["primary", "secondary", "success", "danger", "warning", "info", "dark", "white"]

function getRandomBootstrapColor() {
    return BOOTSTRAP_COLORS[Math.floor(Math.random() * BOOTSTRAP_COLORS.length)];
}

function sendJson(endpoint, data) {
    if (endpoint.startsWith("/")) {
        endpoint = endpoint.substr(1, endpoint.length);
    }
    stompClient.send("/app/" + endpoint, {}, JSON.stringify(data));
}

function chunk(str, n) {
    let ret = [];
    let i;
    let len;

    for (i = 0, len = str.length; i < len; i += n) {
        ret.push(str.substr(i, n))
    }

    return ret
}

