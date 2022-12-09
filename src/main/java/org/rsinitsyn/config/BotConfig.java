package org.rsinitsyn.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
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

    // TODO Apply only when return Compliment
    @Bean
    @Qualifier("complimentVoteInlineKeyboardMarkup")
    public InlineKeyboardMarkup complimentVoteInlineKeyboardMarkup() {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(simpleKeyBoardButton("Да", "COMPLIMENT_YES_BUTTON"));
        buttons.add(simpleKeyBoardButton("Нет", "COMPLIMENT_NO_BUTTON"));

        return InlineKeyboardMarkup.builder()
                .keyboardRow(buttons)
                .build();
    }

    private InlineKeyboardButton simpleKeyBoardButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }
}
