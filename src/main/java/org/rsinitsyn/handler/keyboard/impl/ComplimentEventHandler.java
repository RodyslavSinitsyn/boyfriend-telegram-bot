package org.rsinitsyn.handler.keyboard.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.handler.keyboard.KeyBoardEvent;
import org.rsinitsyn.handler.keyboard.KeyBoardEventHandler;
import org.rsinitsyn.model.MessageWrapper;
import org.rsinitsyn.model.TelegramUserSession;
import org.rsinitsyn.service.ComplimentService;
import org.rsinitsyn.service.LocaleMessageService;
import org.rsinitsyn.utils.Emoji;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
@Slf4j
public class ComplimentEventHandler implements KeyBoardEventHandler {

    private final ComplimentService complimentService;
    private final LocaleMessageService localeMessageService;

    @Override
    public SendMessage handleEvent(MessageWrapper messageWrapper) {
        Message message = messageWrapper.getMessage();
        TelegramUserSession session = messageWrapper.getSession();

        String compliment = complimentService.get(message.getFrom());

        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(localeMessageService.getMessage(
                        "template.compliment",
                        session.getLocale(),
                        compliment,
                        Emoji.HEART.getValue()
                ))
                .build();
    }

    @Override
    public KeyBoardEvent event() {
        return KeyBoardEvent.COMPLIMENT;
    }
}
