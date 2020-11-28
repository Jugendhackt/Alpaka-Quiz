package de.jugendhackt.koeln.alpakaquiz.socket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.jugendhackt.koeln.alpakaquiz.data.Games;
import de.jugendhackt.koeln.alpakaquiz.data.Quiz;
import de.jugendhackt.koeln.alpakaquiz.data.QuizState;
import de.jugendhackt.koeln.alpakaquiz.data.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class SocketConnectionHandler {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/join")
    @SendTo("/topic/greetings")
    public void joinGame(String message) {
        JsonObject json = JsonParser.parseString(message).getAsJsonObject();
        if (!json.has("quiz_pin") || !json.has("team_name")) {
            return;
        }
        int quizPin;
        String teamName;
        try {
            quizPin = json.get("quiz_pin").getAsInt();
            teamName = HtmlUtils.htmlEscape(json.get("team_name").getAsString());
        } catch (Exception e) {
            e.printStackTrace();
            sendError("Bad input");
            return;
        }
        Quiz quiz = Games.games.get(quizPin);
        if (quiz == null) {
            // ToDO
            sendError("Unknown Quiz");
            return;
        }
        if (quiz.getState() != QuizState.WAITING_FOR_CLIENTS) {
            sendError("Quiz started already");
            return;
        }
        Team team = quiz.getTeams().get(teamName);
        if (team != null) {
            sendError("Team joined already");
            return;
        }
        quiz.getTeams().put(teamName, new Team(teamName));
        System.out.printf("Add team %s to quiz %s%n", teamName, quizPin);
    }

    public void sendError(String message) {
        JsonObject payload = new JsonObject();
        payload.addProperty("status", "failed");
        payload.addProperty("message", message);
        sendJson("/join/error", payload);
    }

    public void sendJson(String channel, JsonObject jsonObject) {
        template.convertAndSend(channel, new Gson().toJson(jsonObject));
    }
}
