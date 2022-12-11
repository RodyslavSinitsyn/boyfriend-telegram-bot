package org.rsinitsyn.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class JokeService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public String get(User user) {
        return getJokeFromApi();
    }

    @SneakyThrows
    public String getJokeFromApi() {
        ResponseEntity<String> response = webClient.get()
                .uri("http://free-generator.ru/generator.php?action=joke")
                .retrieve()
                .toEntity(String.class)
                .block();
        log.debug("Response from Joke API: {}", response);


        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        String text = jsonNode.get("joke").get("joke").asText();
        return format(text);
    }

    private String format(String text) {
        return StringUtils.capitalize(text.replaceAll("<br>", "\n"));
    }
}
