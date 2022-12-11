package org.rsinitsyn.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import lombok.SneakyThrows;
import org.rsinitsyn.dto.AssetVoteDto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.AUDIO;
import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.BACK_TO_MENU;
import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.COMPLIMENT;
import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.DOCUMENT;
import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.IMAGE;
import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.JOKE;
import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.RANDOM_ACTION;
import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.VIDEO;

// TODO utility class?
public class BotComponents {

    private final static ObjectMapper MAPPER = new ObjectMapper();

    public static ReplyKeyboardMarkup mainMenuKeyboardMarkup() {
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(rowOf(new KeyboardButton(COMPLIMENT.getText())))
                .keyboardRow(rowOf(new KeyboardButton(JOKE.getText())))
                .keyboardRow(rowOf(new KeyboardButton(RANDOM_ACTION.getText())))
                .build();
    }

    public static ReplyKeyboardMarkup randomActionKeyBoardMarkup() {
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(rowOf(new KeyboardButton(VIDEO.getText()), new KeyboardButton(AUDIO.getText())))
                .keyboardRow(rowOf(new KeyboardButton(DOCUMENT.getText()), new KeyboardButton(IMAGE.getText())))
                .keyboardRow(rowOf(new KeyboardButton(BACK_TO_MENU.getText())))
                .build();
    }

    @SneakyThrows
    public static InlineKeyboardMarkup complimentVoteInlineKeyboardMarkup(InlineKeyboardButton voteButton) {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(voteButton))
                .build();
    }

    @SneakyThrows
    public static InlineKeyboardMarkup complimentVoteInlineKeyboardMarkup() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(voteInlineKeyboardButton()))
                .build();
    }

    public static InlineKeyboardButton voteInlineKeyboardButton() {
        return voteInlineKeyboardButton(new AssetVoteDto("vote", false), "Нравится");
    }

    @SneakyThrows
    public static InlineKeyboardButton voteInlineKeyboardButton(AssetVoteDto voteDto, String text) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(MAPPER.writeValueAsString(voteDto));
        return button;
    }

    private static KeyboardRow rowOf(KeyboardButton... buttons) {
        return new KeyboardRow(Arrays.asList(buttons));
    }
}
