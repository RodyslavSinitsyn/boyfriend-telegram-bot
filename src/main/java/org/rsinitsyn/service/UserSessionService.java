package org.rsinitsyn.service;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.model.TelegramUserSession;
import org.rsinitsyn.props.BotProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// stateless
@Service
@Slf4j
public class UserSessionService {

    private final Map<Long, TelegramUserSession> sessions = new ConcurrentHashMap<>();

    private final BotProperties botProperties;

    @Autowired
    public UserSessionService(BotProperties botProperties) {
        this.botProperties = botProperties;
    }

    public TelegramUserSession getOrCreate(Long chatId) {
        return Optional.ofNullable(sessions.get(chatId))
                .orElseGet(() -> create(chatId));
    }

    public TelegramUserSession get(Long chatId) {
        return sessions.get(chatId);
    }

    public TelegramUserSession create(Long chatId) {
        sessions.put(chatId, defaultSession());
        log.info("User session saved, chatId: {}", chatId);
        return defaultSession();
    }

    private TelegramUserSession defaultSession() {
        TelegramUserSession session = new TelegramUserSession();
        session.setLocale(Locale.forLanguageTag(botProperties.getDefaultLocale()));
        return session;
    }
}
