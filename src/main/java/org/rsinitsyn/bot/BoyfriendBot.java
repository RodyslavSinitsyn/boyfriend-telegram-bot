package org.rsinitsyn.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.props.BotProperties;
import org.rsinitsyn.service.BoyfriendBotApi;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoyfriendBot extends TelegramLongPollingBot {

    private final BoyfriendBotApi botApi;
    private final BotProperties botProperties;

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        // delegate request to process business logic
        botApi.handleUpdate(update);
    }
}
