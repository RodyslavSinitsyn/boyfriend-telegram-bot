package org.rsinitsyn.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.entity.RawCompliment;
import org.rsinitsyn.repository.RawComplimentRepository;
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
    private final RawComplimentRepository rawComplimentRepository;

    @SneakyThrows
    public String get(User user) {
        String complimentText = getComplimentFromApi();

        Optional<RawCompliment> complimentFromDb =
                rawComplimentRepository.findByText(complimentText);

        if (complimentFromDb.isEmpty()) {
            RawCompliment rawCompliment = new RawCompliment();
            rawCompliment.setText(complimentText);
            rawComplimentRepository.save(rawCompliment);
            log.debug("Compliment saved: {}", complimentText);
        }

        return complimentText;
    }

    record ComplimentDto(String id, String text) {
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

        ComplimentDto complimentDto = objectMapper.readValue(response.getBody(), ComplimentDto.class);

        return complimentDto.text();
    }

}
