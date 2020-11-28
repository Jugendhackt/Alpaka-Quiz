client();

function client() {
    connect(onConnect);
}

const QUIZ_PIN = $("#quiz_pin");

function joinGame() {
    sendJson("/join", {'quiz_pin': QUIZ_PIN.val(), 'team_name': $("#team_name").val()});
}

const joinElement = $("#join");


let quizId;

const elements = {
    LOADER: 1,
    JOIN_QUIZ: 2,
};

let currentElement = elements.LOADER;

function switchToElement(element) {
    loaderElement.css("display", "none");
    joinElement.css("display", "none");

    if (element === elements.JOIN_QUIZ) {
        joinElement.css("display", "inline");
    } else {
        loaderElement.css("display", "inline");
    }
    currentElement = element;
}

function onConnect() {
    stompClient.subscribe('/user/join/quiz', function (data) {
        data = JSON.parse(data.body)
        if (data["status"] === "failed") {
            $('#joinErrorModal').modal('show')
            $('#joinErrorModalText').text(data["message"])
        } else if (data["status"] === "success") {
            quizId = data["quiz_id"]
            switchToElement(elements.LOADER);
        }
    });
    switchToElement(elements.JOIN_QUIZ);
}
