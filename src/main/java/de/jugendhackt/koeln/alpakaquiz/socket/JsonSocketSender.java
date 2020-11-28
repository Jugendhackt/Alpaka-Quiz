package de.jugendhackt.koeln.alpakaquiz.socket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class JsonSocketSender {
    public static JsonSocketSender INSTANCE;

    @Autowired
    public SimpMessagingTemplate template;

    public JsonSocketSender() {
        INSTANCE = this;
    }

    public void sendJson(Principal principal, String channel, JsonObject jsonObject) {
        template.convertAndSendToUser(principal.getName(), channel, new Gson().toJson(jsonObject));
    }
}
