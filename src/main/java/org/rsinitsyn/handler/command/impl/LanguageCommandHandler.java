package org.rsinitsyn.handler.command.impl;

import lombok.RequiredArgsConstructor;
import org.rsinitsyn.handler.command.CommandHandler;
import org.rsinitsyn.service.LocaleMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class LanguageCommandHandler implements CommandHandler {

    private final LocaleMessageService localeMessageService;

    @Override
    public SendMessage handleCommand(Message message) {
        localeMessageService.updateLocale();

        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text(localeMessageService.getMessage("reply.localeUpdate"))
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
