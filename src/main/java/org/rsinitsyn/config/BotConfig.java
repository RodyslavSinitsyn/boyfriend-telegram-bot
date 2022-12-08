package org.rsinitsyn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import static org.rsinitsyn.config.KeyBoardCommand.COMPLIMENT;
import static org.rsinitsyn.config.KeyBoardCommand.INSULT;

@Configuration
public class BotConfig {

    @Bean
    public SetMyCommands botCommands() {
        return SetMyCommands.builder()
                .command(new BotCommand("/start", "get greeting message"))
                .command(new BotCommand("/help", "get bot description"))
                .scope(new BotCommandScopeDefault())
                .build();
    }

    @Bean
    public ReplyKeyboardMarkup defaultReplyKeyboardMarkup() {
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(COMPLIMENT.ordinal(), new KeyboardButton(COMPLIMENT.getText()));
        firstRow.add(INSULT.ordinal(), new KeyboardButton(INSULT.getText()));

        return ReplyKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .build();
    }
}
