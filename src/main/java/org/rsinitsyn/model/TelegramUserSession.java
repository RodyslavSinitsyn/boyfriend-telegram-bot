package org.rsinitsyn.model;

import java.util.Locale;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// This is prototype class
@Getter
@Setter
@NoArgsConstructor
public class TelegramUserSession {

    private Long chatId;
    private String username;
    private Locale locale;
}
