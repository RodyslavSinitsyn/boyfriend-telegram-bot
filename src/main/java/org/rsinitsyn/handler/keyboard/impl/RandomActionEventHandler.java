package org.rsinitsyn.handler.keyboard.impl;

import org.rsinitsyn.components.BotComponents;
import org.rsinitsyn.handler.keyboard.KeyBoardEvent;
import org.rsinitsyn.handler.keyboard.KeyBoardEventHandler;
import org.rsinitsyn.model.MessageWrapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class RandomActionEventHandler implements KeyBoardEventHandler<SendMessage> {
    @Override
    public SendMessage handleEvent(MessageWrapper messageWrapper) {
        Message message = messageWrapper.getMessage();

        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text("Твой выбор....")
                .replyMarkup(BotComponents.randomActionKeyBoardMarkup())
                .build();
    }

    @Override
    public KeyBoardEvent event() {
        return KeyBoardEvent.RANDOM_ACTION;
    }
}
