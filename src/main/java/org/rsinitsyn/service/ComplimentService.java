package org.rsinitsyn.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplimentService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public String get(User user) {
        HttpEntity httpEntity = new HttpEntity(Map.of());

        ResponseEntity<String> response = restTemplate.exchange(
                "https://ultragenerator.com/compliment/handler.php",
                HttpMethod.POST,
                httpEntity,
                String.class,
                Collections.emptyMap()
        );
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        log.info("Response  from compliment server: {}", jsonNode);
        return jsonNode.get("text").asText();
    }
}
