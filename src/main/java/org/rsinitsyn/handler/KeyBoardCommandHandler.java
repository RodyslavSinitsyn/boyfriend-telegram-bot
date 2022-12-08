package org.rsinitsyn.handler;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.rsinitsyn.config.KeyBoardCommand;
import org.rsinitsyn.service.ComplimentService;
import org.rsinitsyn.service.InsultService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeyBoardCommandHandler {

    private final ComplimentService complimentService;
    private final InsultService insultService;

    public Optional<String> getResponseByText(String keyBoardText) {
        KeyBoardCommand command = KeyBoardCommand.ofText(keyBoardText);

        String resultText;

        if (command.equals(KeyBoardCommand.COMPLIMENT)) {
            resultText = complimentService.get();
        } else if (command.equals(KeyBoardCommand.INSULT)) {
            resultText = insultService.get();
        } else {
            resultText = null;
        }

        return Optional.ofNullable(resultText);
    }
}
