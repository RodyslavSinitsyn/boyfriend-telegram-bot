package org.rsinitsyn.components;

import java.util.Arrays;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.AUDIO;
import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.BACK_TO_MENU;
import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.COMPLIMENT;
import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.DOCUMENT;
import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.IMAGE;
import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.INSULT;
import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.RANDOM_ACTION;
import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.VIDEO;

// TODO utility class?
public class KeyBoardComponents {

    public static ReplyKeyboardMarkup mainMenuKeyboardMarkup() {
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(rowOf(new KeyboardButton(COMPLIMENT.getText())))
                .keyboardRow(rowOf(new KeyboardButton(INSULT.getText())))
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

    private static KeyboardRow rowOf(KeyboardButton... buttons) {
        return new KeyboardRow(Arrays.asList(buttons));
    }
}
