package com.api.hackathon.model.dto;

import java.util.List;

public class ChatDtos {

    public static class Message {
        public String role;
        public String content;
    }

    public static class ChatRequest {
        public String model;
        public List<Message> messages;
        public Double temperature;
        public Integer max_tokens;
    }

    public static class ChatResponse {
        public String reply;
    }
}
