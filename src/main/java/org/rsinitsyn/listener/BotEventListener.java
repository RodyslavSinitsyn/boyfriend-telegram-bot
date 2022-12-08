package org.rsinitsyn.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.bot.BoyfriendBot;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@RequiredArgsConstructor
@Slf4j
public class BotEventListener {

    private final BoyfriendBot bot;
    private final SetMyCommands botCommands;

    @EventListener(ContextRefreshedEvent.class)
    public void initBot() {
        try {
            bot.execute(botCommands);

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);

            log.info("BoyfriendBot initialized");
        } catch (TelegramApiException e) {
            log.error("Error during bot initializing", e);
            throw new RuntimeException(e);
        }
    }

    @EventListener(ContextClosedEvent.class)
    public void onShutdown() {
        bot.notifyOnShutdown();
    }
}
