package de.jugendhackt.koeln.alpakaquiz.pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Startpage {
    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }
}
