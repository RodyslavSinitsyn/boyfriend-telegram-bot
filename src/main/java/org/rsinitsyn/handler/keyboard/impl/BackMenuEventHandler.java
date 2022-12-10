package org.rsinitsyn.handler.keyboard.impl;

import org.rsinitsyn.components.KeyBoardComponents;
import org.rsinitsyn.handler.keyboard.KeyBoardEvent;
import org.rsinitsyn.handler.keyboard.KeyBoardEventHandler;
import org.rsinitsyn.model.MessageWrapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class BackMenuEventHandler implements KeyBoardEventHandler<SendMessage> {
    @Override
    public SendMessage handleEvent(MessageWrapper messageWrapper) {
        return SendMessage.builder()
                .chatId(messageWrapper.getMessage().getChatId())
                .replyMarkup(KeyBoardComponents.mainMenuKeyboardMarkup())
                .text("Что хочешь?")
                .build();
    }

    @Override
    public KeyBoardEvent event() {
        return KeyBoardEvent.BACK_TO_MENU;
    }
}
