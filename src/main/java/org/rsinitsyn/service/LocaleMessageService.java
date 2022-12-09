package org.rsinitsyn.service;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class LocaleMessageService {

    private final MessageSource messageSource;
    private final UserSessionService sessionStorage;

    public LocaleMessageService(MessageSource messageSource, UserSessionService sessionStorage) {
        this.messageSource = messageSource;
        this.sessionStorage = sessionStorage;
    }

    public String getMessage(String messageKey, Locale locale) {
        return getMessage(messageKey, locale, null);
    }

    public String getMessage(String messageKey, Locale locale, Object... args) {
        return messageSource.getMessage(messageKey, args, locale);
    }
}
