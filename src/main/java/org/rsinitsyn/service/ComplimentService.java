package org.rsinitsyn.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.entity.RawCompliment;
import org.rsinitsyn.repository.RawComplimentRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addLike(Long id) {
        rawComplimentRepository.findById(id)
                .ifPresent(rawCompliment -> {
                    rawCompliment.setLikes(
                            rawCompliment.getLikes() + 1
                    );
                });
    }

    public void addDislike(Long id) {
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
        log.debug("Response from compliment API: {}", response);

        ComplimentResponseDto complimentResponseDto =
                objectMapper.readValue(response.getBody(), ComplimentResponseDto.class);

        return complimentResponseDto.text();
    }

    record ComplimentResponseDto(String id, String text) {
    }
}
