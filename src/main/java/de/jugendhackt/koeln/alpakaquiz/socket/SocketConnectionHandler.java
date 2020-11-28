package de.jugendhackt.koeln.alpakaquiz.socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.jugendhackt.koeln.alpakaquiz.data.Quiz;
import de.jugendhackt.koeln.alpakaquiz.data.QuizState;
import de.jugendhackt.koeln.alpakaquiz.data.Quizzes;
import de.jugendhackt.koeln.alpakaquiz.data.Team;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Nullable;
import java.security.Principal;

@Controller
public class SocketConnectionHandler {

    @MessageMapping("/join")
    public void joinGame(Principal principal, String message) {
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
            setJoinStatus(principal, JoinStatues.FAILED, "Bad input");
            return;
        }
        Quiz quiz = Quizzes.QUIZ_PIN_MAP.get(quizPin);
        if (quiz == null) {
            // ToDO
            setJoinStatus(principal, JoinStatues.FAILED, "Unknown Quiz");
            return;
        }
        if (quiz.getState() != QuizState.WAITING_FOR_CLIENTS) {
            setJoinStatus(principal, JoinStatues.FAILED, "Quiz started already");
            return;
        }
        Team team = quiz.getTeamByName(teamName);
        if (team != null) {
            setJoinStatus(principal, JoinStatues.FAILED, "Team joined already");
            return;
        }
        quiz.addTeam(new Team(teamName, principal));
        System.out.printf("Add team %s to quiz %s%n", teamName, quizPin);
        setJoinStatus(principal, JoinStatues.JOINED, quiz.getQuizId());
    }

    public void setJoinStatus(Principal principal, JoinStatues status, @Nullable String message) {
        JsonObject payload = new JsonObject();
        if (status == JoinStatues.FAILED) {
            payload.addProperty("status", "failed");
        } else {
            payload.addProperty("status", "success");
            payload.addProperty("quiz_id", message);
        }
        if (message != null) {
            payload.addProperty("message", message);
        }
        JsonSocketSender.INSTANCE.sendJson(principal, "/join/quiz", payload);
    }


    enum JoinStatues {
        FAILED,
        JOINED
    }
}
