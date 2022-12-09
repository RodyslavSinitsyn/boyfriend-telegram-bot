package org.rsinitsyn.handler.command.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.entity.TelegramUser;
import org.rsinitsyn.handler.command.CommandHandler;
import org.rsinitsyn.repository.TelegramUserRepository;
import org.rsinitsyn.service.LocaleMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartCommandHandler implements CommandHandler {

    private final TelegramUserRepository telegramUserRepository;
    private final LocaleMessageService messageService;

    public SendMessage handleCommand(Message message) {
        Chat chat = message.getChat();

        saveIfNotExists(message.getFrom());

        return SendMessage
                .builder()
                .text(messageService.getMessage("reply.start"))
                .chatId(chat.getId())
                .build();
    }

    private void saveIfNotExists(User user) {
        Optional<TelegramUser> telegramUserOpt = telegramUserRepository.findByChatId(user.getId());

        if (telegramUserOpt.isEmpty()) {
            TelegramUser telegramUser = new TelegramUser();
            telegramUser.setUsername(user.getUserName());
            telegramUser.setChatId(user.getId());

            TelegramUser savedUser = telegramUserRepository.save(telegramUser);

            log.debug("Telegram user saved: {}", savedUser);
        }
    }

    @Override
    public String description() {
        return "get greeting message";
    }

    @Override
    public String commandName() {
        return "/start";
    }
}
