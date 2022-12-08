package org.rsinitsyn.handler;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.rsinitsyn.config.KeyBoardCommand;
import org.rsinitsyn.service.ComplimentService;
import org.rsinitsyn.service.InsultService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
public class KeyBoardCommandHandler {

    private final ComplimentService complimentService;
    private final InsultService insultService;

    public Optional<String> getResponseByText(Message message) {
        User user = message.getFrom();
        KeyBoardCommand command = KeyBoardCommand.ofText(message.getText());

        String resultText;

        if (command.equals(KeyBoardCommand.COMPLIMENT)) {
            resultText = complimentService.get(user);
        } else if (command.equals(KeyBoardCommand.INSULT)) {
            resultText = insultService.get(user);
        } else {
            resultText = null;
        }

        return Optional.ofNullable(resultText);
    }
}
