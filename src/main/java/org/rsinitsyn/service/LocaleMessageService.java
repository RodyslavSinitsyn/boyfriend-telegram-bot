package org.rsinitsyn.service;

import java.util.Locale;
import org.rsinitsyn.context.UserSessionStorage;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class LocaleMessageService {

    private final MessageSource messageSource;
    private final UserSessionStorage sessionStorage;

    public LocaleMessageService(MessageSource messageSource, UserSessionStorage sessionStorage) {
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
