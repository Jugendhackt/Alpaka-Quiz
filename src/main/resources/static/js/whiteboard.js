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
    CREATING: 4
};

let currentElement = elements.LOADER;

const questionElement = $("#question");
const joiningElement = $("#joining");
const CREATING_ELEMENT = $("#creating");


function switchToElement(element) {
    loaderElement.css("display", "none");
    joiningElement.css("display", "none");
    questionElement.css("display", "none");
    CREATING_ELEMENT.css("display", "none");

    if (element === elements.QUESTION_SHOW) {
        questionElement.css("display", "inline");
    } else if (element === elements.CREATING) {
        CREATING_ELEMENT.css("display", "inline");
    } else if (element === elements.JOINING) {
        joiningElement.css("display", "inline");
    } else {
        loaderElement.css("display", "inline");
    }
    currentElement = element;
}

const JOINING_QUIZ_PIN = $("#joiningQuizPin");

const QUESTION_QUESTION = $("#questionQuestion");
const QUESTION_TIME = $("#questionTime");
const QUESTION_ANSWER_RED = $("#questionAnswerRed");
const QUESTION_ANSWER_BLUE = $("#questionAnswerBlue");
const QUESTION_ANSWER_GREEN = $("#questionAnswerGreen");
const QUESTION_ANSWER_YELLOW = $("#questionAnswerYellow");

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
    stompClient.subscribe('/user/whiteboard/question', function (data) {
        data = JSON.parse(data.body)

        QUESTION_QUESTION.val(data["question"]);

        let answers = data["answers"];
        if (answers.hasOwnProperty("red")) {
            QUESTION_ANSWER_RED.show();
            QUESTION_ANSWER_RED.text(answers["red"]);
        } else {
            QUESTION_ANSWER_RED.hide();
        }
        if (answers.hasOwnProperty("blue")) {
            QUESTION_ANSWER_BLUE.show();
            QUESTION_ANSWER_BLUE.text(answers["blue"]);
        } else {
            QUESTION_ANSWER_BLUE.hide();
        }
        if (answers.hasOwnProperty("yellow")) {
            QUESTION_ANSWER_YELLOW.show();
            QUESTION_ANSWER_YELLOW.text(answers["yellow"]);
        } else {
            QUESTION_ANSWER_YELLOW.hide();
        }
        if (answers.hasOwnProperty("green")) {
            QUESTION_ANSWER_GREEN.show();
            QUESTION_ANSWER_GREEN.text(answers["green"]);
        } else {
            QUESTION_ANSWER_GREEN.hide();
        }


        quizId = data["quiz_id"]

        switchToElement(elements.QUESTION_SHOW);
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
