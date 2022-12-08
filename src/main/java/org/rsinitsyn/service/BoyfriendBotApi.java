package org.rsinitsyn.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface BoyfriendBotApi {
    void handleUpdate(Update update);
    void notifyOnInit();
    void notifyOnShutdown();
}
