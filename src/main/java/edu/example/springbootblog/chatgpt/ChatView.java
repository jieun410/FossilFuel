package edu.example.springbootblog.chatgpt;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatView {

    @GetMapping("/chatgpt")
    public String index() {
        return "chat/chatgpt";
    }
}
