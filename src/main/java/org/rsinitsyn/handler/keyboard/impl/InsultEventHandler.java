package org.rsinitsyn.handler.keyboard.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.handler.keyboard.KeyBoardEvent;
import org.rsinitsyn.handler.keyboard.KeyBoardEventHandler;
import org.rsinitsyn.model.MessageWrapper;
import org.rsinitsyn.service.InsultService;
import org.rsinitsyn.service.LocaleMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
@RequiredArgsConstructor
@Slf4j
public class InsultEventHandler implements KeyBoardEventHandler {

    private final InsultService insultService;
    private final LocaleMessageService localeMessageService;

    @Override
    public SendMessage handleEvent(MessageWrapper messageWrapper) {
        Message message = messageWrapper.getMessage();
        String insult = insultService.get(message.getFrom());
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(insult)
                .build();
    }

    @Override
    public KeyBoardEvent event() {
        return KeyBoardEvent.INSULT;
    }
}
