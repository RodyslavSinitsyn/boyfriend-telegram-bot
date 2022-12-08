package org.rsinitsyn.listener;

import lombok.RequiredArgsConstructor;
import org.rsinitsyn.service.BoyfriendBotApi;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BotEventListener {

    private final BoyfriendBotApi botApi;

    @EventListener(ContextClosedEvent.class)
    public void onShutdown() {
        botApi.notifyOnShutdown();
    }
}
