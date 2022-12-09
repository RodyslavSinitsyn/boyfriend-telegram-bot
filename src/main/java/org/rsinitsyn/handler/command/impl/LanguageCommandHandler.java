package org.rsinitsyn.handler.command.impl;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.handler.command.CommandHandler;
import org.rsinitsyn.model.MessageWrapper;
import org.rsinitsyn.model.TelegramUserSession;
import org.rsinitsyn.service.LocaleMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
@Slf4j
public class LanguageCommandHandler implements CommandHandler {

    private final LocaleMessageService localeMessageService;

    @Override
    public SendMessage handleCommand(MessageWrapper messageWrapper) {
        Message message = messageWrapper.getMessage();
        TelegramUserSession session = messageWrapper.getSession();

        String changedLanguage = session.getLocale().getLanguage().equals("ru") ?
                "en" : "ru";
        session.setLocale(Locale.forLanguageTag(changedLanguage));

        log.debug("Language updated on {}, chatId: {}", changedLanguage, message.getChatId());

        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text(localeMessageService.getMessage("reply.localeUpdate", session.getLocale()))
                .build();
    }

    @Override
    public String description() {
        return "toggle language ru/en";
    }

    @Override
    public String commandName() {
        return "/lang";
    }
}
