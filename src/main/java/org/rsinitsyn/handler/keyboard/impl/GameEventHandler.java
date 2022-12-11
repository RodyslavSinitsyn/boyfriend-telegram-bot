package org.rsinitsyn.handler.keyboard.impl;

import org.rsinitsyn.handler.keyboard.KeyBoardEvent;
import org.rsinitsyn.handler.keyboard.KeyBoardEventHandler;
import org.rsinitsyn.model.MessageWrapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendGame;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class GameEventHandler implements KeyBoardEventHandler<SendGame> {
    @Override
    public SendGame handleEvent(MessageWrapper messageWrapper) {
        Message message = messageWrapper.getMessage();

        return SendGame.builder()
                .chatId(message.getChatId())
                .gameShortName("bfbot_game")
                .build();
    }

    @Override
    public KeyBoardEvent event() {
        return KeyBoardEvent.GAME;
    }
}
