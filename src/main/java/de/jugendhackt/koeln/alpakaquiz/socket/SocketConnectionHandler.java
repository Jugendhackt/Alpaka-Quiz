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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Nullable;
import java.security.Principal;

@Controller
public class SocketConnectionHandler {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/join")
    public void joinGame(Principal sessionId, String message) {
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
            setJoinStatus(sessionId, JoinStatues.FAILED, "Bad input");
            return;
        }
        Quiz quiz = Games.games.get(quizPin);
        if (quiz == null) {
            // ToDO
            setJoinStatus(sessionId, JoinStatues.FAILED, "Unknown Quiz");
            return;
        }
        if (quiz.getState() != QuizState.WAITING_FOR_CLIENTS) {
            setJoinStatus(sessionId, JoinStatues.FAILED, "Quiz started already");
            return;
        }
        Team team = quiz.getTeams().get(teamName);
        if (team != null) {
            setJoinStatus(sessionId, JoinStatues.FAILED, "Team joined already");
            return;
        }
        quiz.getTeams().put(teamName, new Team(teamName));
        System.out.printf("Add team %s to quiz %s%n", teamName, quizPin);
        setJoinStatus(sessionId, JoinStatues.JOINED, null);
    }

    public void setJoinStatus(Principal sessionId, JoinStatues status, @Nullable String message) {
        JsonObject payload = new JsonObject();
        if (status == JoinStatues.FAILED) {
            payload.addProperty("status", "failed");
        } else {
            payload.addProperty("status", "success");
        }
        if (message != null) {
            payload.addProperty("message", message);
        }
        sendJson(sessionId, "/join/error", payload);
    }

    public void sendJson(Principal sessionId, String channel, JsonObject jsonObject) {
        template.convertAndSendToUser(sessionId.getName(), channel, new Gson().toJson(jsonObject));
    }

    enum JoinStatues {
        FAILED,
        JOINED
    }
}
