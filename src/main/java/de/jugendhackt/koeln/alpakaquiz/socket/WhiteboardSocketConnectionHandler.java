package de.jugendhackt.koeln.alpakaquiz.socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.jugendhackt.koeln.alpakaquiz.data.Quiz;
import de.jugendhackt.koeln.alpakaquiz.data.Quizzes;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WhiteboardSocketConnectionHandler {

    @MessageMapping("/whiteboard/create")
    public void createGame(Principal sessionId) {
        Quiz quiz = new Quiz(sessionId);
        int quizPin = Quizzes.createQuiz(quiz);

        JsonObject response = new JsonObject();
        response.addProperty("quiz_pin", quizPin);
        response.addProperty("quiz_id", quiz.getQuizId());

        JsonSocketSender.INSTANCE.sendJson(sessionId, "/whiteboard/create", response);

    }

    @MessageMapping("/whiteboard/startGame")
    public void startGame(Principal sessionId, String message) {
        JsonObject data = JsonParser.parseString(message).getAsJsonObject();
        Quiz quiz = Quizzes.getQuizById(data.get("quiz_id").getAsString());
        if (!quiz.isWhiteboard(sessionId)) {
            return;
        }
        quiz.start();
    }

    @MessageMapping("/whiteboard/nextQuestion")
    public void nextQuestion(Principal sessionId, String message) {
        JsonObject data = JsonParser.parseString(message).getAsJsonObject();
        Quiz quiz = Quizzes.getQuizById(data.get("quiz_id").getAsString());
        if (!quiz.isWhiteboard(sessionId)) {
            return;
        }
        quiz.nextQuestion();
    }

}
