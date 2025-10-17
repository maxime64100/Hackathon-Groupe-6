package com.api.hackathon.controller;

import com.api.hackathon.model.dto.ChatDtos.ChatRequest;
import com.api.hackathon.model.dto.ChatDtos.ChatResponse;
import com.api.hackathon.service.ChatProxyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatProxyService chatProxy;

    public ChatController(ChatProxyService chatProxy) {
        this.chatProxy = chatProxy;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest req) {
        List<Map<String, String>> messages = new ArrayList<>();
        if (req.messages == null || req.messages.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        for (var m : req.messages) {
            var mm = new HashMap<String, String>();
            mm.put("role", m.role);
            mm.put("content", m.content);
            messages.add(mm);
        }

        var body = chatProxy.buildOpenAIBody(messages, req.model, req.temperature, req.max_tokens);

        String reply = chatProxy.sendToOpenAI(body);

        ChatResponse out = new ChatResponse();
        out.reply = reply;
        return ResponseEntity.ok(out);
    }
}
