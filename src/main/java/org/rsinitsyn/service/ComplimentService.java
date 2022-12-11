package org.rsinitsyn.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.rsinitsyn.entity.ComplimentGrade;
import org.rsinitsyn.entity.RawCompliment;
import org.rsinitsyn.entity.UserCompliment;
import org.rsinitsyn.repository.RawComplimentRepository;
import org.rsinitsyn.repository.UserComplimentRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplimentService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final RawComplimentRepository rawComplimentRepository;
    private final UserComplimentRepository userComplimentRepository;

    @SneakyThrows
    public RawCompliment get(User user) {
        String complimentText = getComplimentFromApi();

        return rawComplimentRepository.findByText(complimentText)
                .orElseGet(() -> {
                    RawCompliment rawCompliment = new RawCompliment();
                    rawCompliment.setText(complimentText);
                    RawCompliment saved = rawComplimentRepository.save(rawCompliment);
                    log.debug("Compliment saved: {}", complimentText);
                    return saved;
                });
    }

    // TODO Later should save statistic to DB
    @Transactional
    public void updateGrade(Long userId, Long complimentId, ComplimentGrade grade) {
        userComplimentRepository.findByUserIdAndComplimentId(
                userId, complimentId
        ).ifPresentOrElse(
                userCompliment -> userCompliment.setGrade(grade),
                () -> {
                    UserCompliment userCompliment = new UserCompliment();
                    userCompliment.setComplimentId(complimentId);
                    userCompliment.setUserId(userId);
                    userCompliment.setGrade(grade);
                    userComplimentRepository.save(userCompliment);
                });
    }

    @SneakyThrows
    private String getComplimentFromApi() {
        HttpEntity httpEntity = new HttpEntity(Map.of());

        ResponseEntity<String> response = restTemplate.exchange(
                "https://ultragenerator.com/compliment/handler.php",
                HttpMethod.POST,
                httpEntity,
                String.class,
                Collections.emptyMap()
        );
        log.debug("Response from Compliment API: {}", response);

        ComplimentResponseDto complimentResponseDto =
                objectMapper.readValue(response.getBody(), ComplimentResponseDto.class);

        return format(complimentResponseDto.text());
    }

    private String format(String text) {
        return StringUtils.capitalize(text);
    }

    record ComplimentResponseDto(String id, String text) {
    }
}
