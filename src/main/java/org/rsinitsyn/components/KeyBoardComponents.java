package org.rsinitsyn.components;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.COMPLIMENT;
import static org.rsinitsyn.handler.keyboard.KeyBoardEvent.INSULT;

// TODO utility class?
public class KeyBoardComponents {

    public static ReplyKeyboardMarkup mainMenuKeyboardMarkup() {
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(0, new KeyboardButton(COMPLIMENT.getText()));

        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(0, new KeyboardButton(INSULT.getText()));

        return ReplyKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .keyboardRow(secondRow)
                .build();
    }
}
