package org.rsinitsyn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Configuration
public class BotConfig {

    // TODO Init via list of CommandHandlers
    @Bean
    public SetMyCommands botCommands() {
        return SetMyCommands.builder()
                .command(new BotCommand("/start", "get greeting message"))
                .command(new BotCommand("/lang", "toggle lang ru/en"))
                .scope(new BotCommandScopeDefault())
                .build();
    }

    private InlineKeyboardButton simpleKeyBoardButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }
}
