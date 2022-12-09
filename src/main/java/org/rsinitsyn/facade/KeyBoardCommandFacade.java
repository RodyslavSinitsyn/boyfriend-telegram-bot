package org.rsinitsyn.facade;

import lombok.RequiredArgsConstructor;
import org.rsinitsyn.config.KeyBoardCommand;
import org.rsinitsyn.service.ComplimentService;
import org.rsinitsyn.service.InsultService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
public class KeyBoardCommandFacade {

    private final ComplimentService complimentService;
    private final InsultService insultService;

    public BotApiMethod<?> handle(Message message) {
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

        return SendMessage.builder()
                .text(resultText)
                .chatId(message.getChatId())
                .build();
    }
}
