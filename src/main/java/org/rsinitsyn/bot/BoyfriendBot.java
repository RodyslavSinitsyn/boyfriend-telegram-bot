package org.rsinitsyn.bot;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.api.BoyfriendBotApi;
import org.rsinitsyn.facade.TelegramBotFacade;
import org.rsinitsyn.props.BotProperties;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
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
        try {
            PartialBotApiMethod<?> botApiMethod = facade.handleUpdate(update);
            if (Objects.nonNull(botApiMethod)) {
                send(botApiMethod);
            } else {
                log.debug("Does not need to send response back");
            }
        } catch (Exception exception) {
            log.error("Failed to handle user message", exception);
        }
    }

    @SneakyThrows
    @Override
    public void notifyOnStartup() {
        if (botProperties.isNotifyOnStartup()) {
            for (SendMessage message : facade.getNotificationMessagesOnStartup()) {
                send(message);
            }
        }
    }

    @SneakyThrows
    @Override
    public void notifyOnShutdown() {
        if (botProperties.isNotifyOnShutdown()) {
            for (SendMessage message : facade.getNotificationMessagesOnShutdown()) {
                send(message);
            }
        }
    }

    private void send(PartialBotApiMethod<?> method) {
        // OK...It's kinda trash, but let's leave it as it is fow now :)
        try {
            if (method instanceof BotApiMethod<?> botApiMethod) {
                 execute(botApiMethod);
            } else if (method instanceof SendDocument document) {
                execute(document);
            } else if (method instanceof SendVideo video) {
                execute(video);
            } else if (method instanceof SendAudio audio) {
                execute(audio);
            } else if (method instanceof SendPhoto photo) {
                execute(photo);
            }
        } catch (TelegramApiException e) {
            log.error("Message has not been sent.", e);
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
