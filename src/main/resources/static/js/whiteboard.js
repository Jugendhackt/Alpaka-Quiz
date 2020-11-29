whiteboard();

function whiteboard() {
    connect(onConnect);

}


const JOIN_TEAMS_ELEMENT = $("#joiningTeams");
const JOIN_TEAMS_COUNT = $("#joiningTeamsNumber");

function addTeam(name) {
    if (currentElement !== elements.JOINING) {
        return;
    }
    JOIN_TEAMS_COUNT.text(parseInt(JOIN_TEAMS_COUNT.text()) + 1);

    let color = getRandomBootstrapColor();
    while (color === "white") {
        color = getRandomBootstrapColor();
    }
    JOIN_TEAMS_ELEMENT.append('<span class="m-1 alert alert-' + color + '">' + name + '</span>')


}

const elements = {
    LOADER: 1,
    QUESTION_SHOW: 2,
    JOINING: 3,
    CREATING: 4,
    ANSWERS: 5,
    FINALS: 6
};

let currentElement = elements.LOADER;

const JOINING_ELEMENT = $("#joining");
const CREATING_ELEMENT = $("#creating");


function switchToElement(element) {
    LOADER_ELEMENT.css("display", "none");
    JOINING_ELEMENT.css("display", "none");
    QUESTION_ELEMENT.css("display", "none");
    CREATING_ELEMENT.css("display", "none");
    ANSWER_ELEMENT.css(("display"), "none");
    FINAL_ELEMENT.css(("display"), "none");

    if (element === elements.ANSWERS) {
        ANSWER_ELEMENT.css("display", "inline");
    } else if (element === elements.FINALS) {
        FINAL_ELEMENT.css("display", "inline");
    } else if (element === elements.QUESTION_SHOW) {
        QUESTION_ELEMENT.css("display", "inline");
    } else if (element === elements.CREATING) {
        CREATING_ELEMENT.css("display", "inline");
    } else if (element === elements.JOINING) {
        JOINING_ELEMENT.css("display", "inline");
    } else {
        LOADER_ELEMENT.css("display", "inline");
    }
    currentElement = element;
}

const JOINING_QUIZ_PIN = $("#joiningQuizPin");


let quizId;

function onConnect() {
    stompClient.subscribe('/user/whiteboard/addTeam', function (data) {
        data = JSON.parse(data.body)
        addTeam(data["name"])
    });
    stompClient.subscribe('/user/whiteboard/create', function (data) {
        data = JSON.parse(data.body)
        quizId = data["quiz_id"]
        JOINING_QUIZ_PIN.text(chunk(data["quiz_pin"].toString(), 3).join(' '));

        switchToElement(elements.JOINING);
    });

    switchToElement(elements.CREATING);
}

function createQuiz() {
    sendJson("/whiteboard/create", {});
    switchToElement(elements.LOADER);
}

function startQuiz() {
    sendJson("/whiteboard/startGame", {"quiz_id": quizId});
    switchToElement(elements.LOADER);
}

function nextQuestion() {
    sendJson("/whiteboard/nextQuestion", {"quiz_id": quizId});
    switchToElement(elements.LOADER);
}
