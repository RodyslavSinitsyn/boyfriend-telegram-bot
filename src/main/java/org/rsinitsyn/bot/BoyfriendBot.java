package org.rsinitsyn.bot;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.api.BoyfriendBotApi;
import org.rsinitsyn.facade.TelegramBotFacade;
import org.rsinitsyn.props.BotProperties;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoyfriendBot extends TelegramLongPollingBot implements BoyfriendBotApi {

    private final BotProperties botProperties;
    private final TelegramBotFacade facade;

    @Override
    public void handleUpdate(Update update) {
        BotApiMethod<?> botApiMethod = facade.handleUpdate(update);
        if (Objects.nonNull(botApiMethod)) {
            send(botApiMethod);
        } else {
            log.debug("Does not need to send response back");
        }
    }

    private void send(BotApiMethod<?> method) {
        try {
            execute(method);
        } catch (TelegramApiException e) {
            log.error("Message has not been sent.", e);
        }
    }

    @Override
    public void notifyOnInit() {
        if (botProperties.getNotifyOnStartup()) {
            // TODO Notify
        }
    }

    @Override
    public void notifyOnShutdown() {
        if (botProperties.getNotifyOnShutdown()) {
            // TODO Notify
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        // delegate request to process business logic
        handleUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }
}
