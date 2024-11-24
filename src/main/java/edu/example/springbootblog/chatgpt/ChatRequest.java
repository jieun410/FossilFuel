package edu.example.springbootblog.chatgpt;

public class ChatRequest {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    } // 프롬프트 엔지니어링 시에 사용
}

