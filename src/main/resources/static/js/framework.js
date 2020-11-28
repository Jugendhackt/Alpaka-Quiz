let stompClient = null;


const QUESTION_QUESTION = $("#questionQuestion");
const QUESTION_TIME = $("#questionTime");
const QUESTION_ANSWER_RED = $("#questionAnswerRed");
const QUESTION_ANSWER_BLUE = $("#questionAnswerBlue");
const QUESTION_ANSWER_GREEN = $("#questionAnswerGreen");
const QUESTION_ANSWER_YELLOW = $("#questionAnswerYellow");
const ANSWERS_QUESTION = $("#answersQuestion");
const ANSWERS_CHART = $("#answersChart");


function connect(callback) {
    let socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        callback();
        stompClient.subscribe('/user/whiteboard/question', function (data) {
            data = JSON.parse(data.body)

            QUESTION_QUESTION.text(data["question"]);
            ANSWERS_QUESTION.text(data["question"]);

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

            switchToElement(elements.QUESTION_SHOW);
        });
        stompClient.subscribe('/user/whiteboard/answers', function (data) {
            data = JSON.parse(data.body)
            let chartColorNames = [];
            let chartData = [];
            let chartColors = [];

            for (const [color, number] of Object.entries(data["answers"])) {
                chartData.push(number);
                chartColorNames.push(color);
                chartColors.push(hexByColor(color));
            }

            let myBarChart = new Chart(ANSWERS_CHART, {
                type: 'bar',
                data: {
                    labels: chartColorNames,
                    datasets: [{
                        label: "# of votes",
                        data: chartData,
                        backgroundColor: chartColors
                    }]
                },
                options: {
                    scales: {
                        yAxes: [{
                            ticks: {
                                beginAtZero: true
                            }
                        }]
                    }
                }
            });
            switchToElement(elements.ANSWERS);

        });
    });
}

const LOADER_ELEMENT = $("#loader");
const QUESTION_ELEMENT = $("#question");
const ANSWER_ELEMENT = $("#answers");


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

function hexByColor(color) {
    switch (color) {
        case "red":
            return "#dc3545";
        case "green":
            return "#28a745";
        case "yellow":
            return "#ffc107";
        case "blue":
            return "#007bff";
    }
}
