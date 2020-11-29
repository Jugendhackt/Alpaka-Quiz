const LOADER_ELEMENT = $("#loader");
const QUESTION_ELEMENT = $("#question");
const ANSWER_ELEMENT = $("#answers");
const FINAL_ELEMENT = $("#finals");


const BOOTSTRAP_COLORS = ["primary", "secondary", "success", "danger", "warning", "info", "dark", "light"]

const QUESTION_QUESTION = $("#questionQuestion");
const QUESTION_TIME = $("#questionTime");
const QUESTION_ANSWER_RED = $("#questionAnswerRed");
const QUESTION_ANSWER_BLUE = $("#questionAnswerBlue");
const QUESTION_ANSWER_GREEN = $("#questionAnswerGreen");
const QUESTION_ANSWER_YELLOW = $("#questionAnswerYellow");
const ANSWERS_QUESTION = $("#answersQuestion");
const ANSWERS_CHART = $("#answersChart");
const FINALS_CHARTS = $("#finalCharts");

let stompClient = null;

let ANSWER_CHART_CHART = new Chart(ANSWERS_CHART, {
    type: 'bar',
    options: {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero: true,
                    precision: 0
                }
            }]
        },
        legend: {
            display: false
        }
    }
});

let TIMER_TIMER = -1;
let TIMER_INTERVAL = setInterval(() => {
    if (TIMER_TIMER < 0) {
        return;
    }
    TIMER_TIMER--;
    if (TIMER_TIMER === 0) {
        if (currentElement === elements.QUESTION_SHOW) {
            switchToElement(elements.LOADER);
            return;
        }
    }
    QUESTION_TIME.text(TIMER_TIMER);


}, 1000);


function setTimer(seconds) {
    TIMER_TIMER = seconds;
    QUESTION_TIME.text(TIMER_TIMER);
}

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
            setTimer(20);

            switchToElement(elements.QUESTION_SHOW);
        });
        stompClient.subscribe('/user/whiteboard/answers', function (data) {
            data = JSON.parse(data.body)
            let chartColorNames = [];
            let chartData = [];
            let chartColors = [];
            let borderColor = [];

            for (const [color, count] of Object.entries(data["givenAnswers"])) {
                chartData.push(count);
                chartColors.push(hexByColor(color));
                if (data["correct"] === color) {
                    borderColor.push("rgb(104, 34,139)")
                    chartColorNames.push("[" + data["allAnswers"][color] + "]");
                } else {
                    borderColor.push("rgba(0, 0, 0, 0, 0)")
                    chartColorNames.push(data["allAnswers"][color]);
                }
            }

            ANSWER_CHART_CHART.reset();

            ANSWER_CHART_CHART.data = {
                labels: chartColorNames,
                datasets: [{
                    label: "# of votes",
                    data: chartData,
                    backgroundColor: chartColors,
                    borderColor: borderColor,
                    borderWidth: 20
                }]
            };

            ANSWER_CHART_CHART.update();
            switchToElement(elements.ANSWERS);
        });
        stompClient.subscribe('/user/whiteboard/finals', function (data) {
            data = JSON.parse(data.body)
            let charTeamNames = [];
            let chartData = [];
            let chartColors = [];

            for (const [teamName, score] of Object.entries(data)) {
                chartData.push(score);
                charTeamNames.push(teamName);
                chartColors.push(hexByColor(getRandomBootstrapColor()));
            }

            let myBarChart = new Chart(FINALS_CHARTS, {
                type: 'bar',
                data: {
                    labels: charTeamNames,
                    datasets: [{
                        label: "Points",
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
            switchToElement(elements.FINALS);
        });
    });
}

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
    color = color.toString().toLowerCase();
    switch (color) {
        case "red":
        case "danger":
            return "#dc3545";
        case "green":
        case "success":
            return "#28a745";
        case "yellow":
        case "warning":
            return "#ffc107";
        case "blue":
        case "primary":
            return "#007bff";
        case "grey":
        case "secondary":
            return "#6c757d";
        case "info":
        case "light":
            return "#17a2b8";
        case "dark":
            return "#343a40";
        default:
            return "#123456"
    }
}
