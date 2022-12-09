package org.rsinitsyn.handler.keyboard;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.model.MessageWrapper;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeyBoardEventFacade {

    private final List<KeyBoardEventHandler> keyBoardEventHandlers;

    public BotApiMethod<?> handle(MessageWrapper messageWrapper) {
        Message message = messageWrapper.getMessage();

        // cant be null here
        KeyBoardEvent eventName = KeyBoardEvent.ofText(message.getText());

        log.info("New [event] from username: {}, chatId: {}, event: {}",
                message.getFrom().getUserName(),
                message.getChat().getId(),
                eventName);

        KeyBoardEventHandler keyBoardEventHandler = getHandler(eventName);

        if (keyBoardEventHandler == null) {
            log.warn("EventHandler not implemented yet, chatId: {}, event: {}",
                    message.getChat().getId(),
                    eventName);
            return SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text("In progress to implement...")
                    .build();
        }

        return keyBoardEventHandler.handleEvent(messageWrapper);
    }

    private KeyBoardEventHandler getHandler(KeyBoardEvent event) {
        return keyBoardEventHandlers.stream()
                .filter(keyBoardEventHandler -> keyBoardEventHandler.event().equals(event))
                .findFirst()
                .orElse(null);
    }
}
