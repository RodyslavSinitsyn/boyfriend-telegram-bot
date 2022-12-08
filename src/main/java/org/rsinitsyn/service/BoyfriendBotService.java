package org.rsinitsyn.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class BoyfriendBotService implements BoyfriendBotApi {

    @Override
    public void handleUpdate(Update update) {

    }

    @Override
    public void notifyOnInit() {

    }

    @Override
    public void notifyOnShutdown() {

    }
}
