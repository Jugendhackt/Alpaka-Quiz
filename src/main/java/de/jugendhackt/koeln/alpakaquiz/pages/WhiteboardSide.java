package de.jugendhackt.koeln.alpakaquiz.pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WhiteboardSide {
    @RequestMapping("/wb")
    public String whiteboard(Model model) {
        return "whiteboard";
    }
}
