package org.rsinitsyn.handler.keyboard.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.components.BotComponents;
import org.rsinitsyn.handler.keyboard.KeyBoardEvent;
import org.rsinitsyn.handler.keyboard.KeyBoardEventHandler;
import org.rsinitsyn.model.MessageWrapper;
import org.rsinitsyn.service.JokeService;
import org.rsinitsyn.service.LocaleMessageService;
import org.rsinitsyn.utils.Emoji;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
@RequiredArgsConstructor
@Slf4j
public class JokeEventHandler implements KeyBoardEventHandler<SendMessage> {

    private final JokeService jokeService;
    private final LocaleMessageService localeMessageService;

    @Override
    public SendMessage handleEvent(MessageWrapper messageWrapper) {
        Message message = messageWrapper.getMessage();
        String joke = jokeService.get(message.getFrom());
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(localeMessageService.getMessage("template.joke",
                                messageWrapper.getSession().getLocale(),
                                joke,
                                Emoji.LAUGH.getValue()))
                .parseMode(ParseMode.HTML)
                .replyMarkup(BotComponents.complimentVoteInlineKeyboardMarkup())
                .build();
    }

    @Override
    public KeyBoardEvent event() {
        return KeyBoardEvent.JOKE;
    }
}
