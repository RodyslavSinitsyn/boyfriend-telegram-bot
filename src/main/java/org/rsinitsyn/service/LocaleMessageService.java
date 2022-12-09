package org.rsinitsyn.service;

import java.util.Locale;
import org.rsinitsyn.props.BotProperties;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class LocaleMessageService {

    private final MessageSource messageSource;

    // TODO Move to UserSession
    private Locale locale;


    public LocaleMessageService(MessageSource messageSource, BotProperties botProperties) {
        this.messageSource = messageSource;
        this.locale = Locale.forLanguageTag(botProperties.getDefaultLocale());
    }

    public void updateLocale() {
        String toggledLocalLang = locale.getLanguage().equals("ru") ?
                "en" : "ru";
        this.locale = Locale.forLanguageTag(toggledLocalLang);
    }

    public String getMessage(String messageKey) {
        return getMessage(messageKey, locale);
    }

    public String getMessage(String messageKey, Locale locale, Object... args) {
        return messageSource.getMessage(messageKey, args, locale);
    }
}
