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

    public TelegramUserSession getOrCreate(Long chatId, String username) {
        return Optional.ofNullable(sessions.get(chatId))
                .orElseGet(() -> create(chatId, username));
    }

    public TelegramUserSession get(Long chatId) {
        return sessions.get(chatId);
    }

    public TelegramUserSession create(Long chatId, String username) {
        TelegramUserSession session = defaultSession();
        session.setChatId(chatId);
        session.setUsername(username);
        sessions.put(chatId, session);
        log.info("User session saved, username: {}, chatId: {}", username, chatId);
        return session;
    }

    private TelegramUserSession defaultSession() {
        TelegramUserSession session = new TelegramUserSession();
        session.setLocale(Locale.forLanguageTag(botProperties.getDefaultLocale()));
        return session;
    }
}
