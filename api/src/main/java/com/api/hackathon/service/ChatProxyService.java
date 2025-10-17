package com.api.hackathon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatProxyService {

    @Value("${openai.api-url}")
    private String apiUrl;

    @Value("${openai.model:gpt-4o-mini}")
    private String defaultModel;

    @Value("#{systemEnvironment['OPENAI_API_KEY']}")
    private String openaiKey;

    private final RestTemplate http = new RestTemplate();

    public String sendToOpenAI(Object body) {
        if (openaiKey == null || openaiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY non défini en variable d'environnement.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiKey);

        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> res = http.exchange(apiUrl, HttpMethod.POST, entity, Map.class);

        if (!res.getStatusCode().is2xxSuccessful() || res.getBody() == null) {
            throw new RuntimeException("Appel OpenAI en échec: " + res.getStatusCode());
        }

        try {
            var choices = (java.util.List<Map<String,Object>>) res.getBody().get("choices");
            var msg = (Map<String,Object>) choices.get(0).get("message");
            return (String) msg.get("content");
        } catch (Exception e) {
            throw new RuntimeException("Réponse OpenAI inattendue.", e);
        }
    }

    public Map<String, Object> buildOpenAIBody(
            java.util.List<Map<String, String>> messages,
            String model,
            Double temperature,
            Integer maxTokens
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", (model != null && !model.isBlank()) ? model : defaultModel);
        body.put("messages", messages);
        if (temperature != null) body.put("temperature", temperature);
        if (maxTokens != null) body.put("max_tokens", maxTokens);
        return body;
    }
}
