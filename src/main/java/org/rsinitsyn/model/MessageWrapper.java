package org.rsinitsyn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.rsinitsyn.context.TelegramUserSession;
import org.telegram.telegrambots.meta.api.objects.Message;

@AllArgsConstructor
@Getter
public class MessageWrapper {
    private Message message;
    private TelegramUserSession session;
}
