client();

function client() {
    connect(onConnect);
}


let quizId;

const QUIZ_PIN = $("#quiz_pin");
const JOIN_ELEMENT = $("#join");

function joinGame() {
    sendJson("/join", {'quiz_pin': QUIZ_PIN.val().replace(" ", ""), 'team_name': $("#team_name").val()});
}

function answerQuestion(answer) {
    sendJson("/vote", {'quiz_id': quizId, 'answer': answer});
    switchToElement(elements.LOADER);
}


const elements = {
    LOADER: 1,
    JOIN_QUIZ: 2,
    QUESTION_SHOW: 3,
    ANSWERS: 4
};

let currentElement = elements.LOADER;

function switchToElement(element) {
    LOADER_ELEMENT.css("display", "none");
    JOIN_ELEMENT.css("display", "none");
    QUESTION_ELEMENT.css("display", "none");
    ANSWER_ELEMENT.css(("display"), "none");

    if (element === elements.ANSWERS) {
        ANSWER_ELEMENT.css("display", "inline");
    } else if (element === elements.QUESTION_SHOW) {
        QUESTION_ELEMENT.css("display", "inline");
    } else if (element === elements.JOIN_QUIZ) {
        JOIN_ELEMENT.css("display", "inline");
    } else {
        LOADER_ELEMENT.css("display", "inline");
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
