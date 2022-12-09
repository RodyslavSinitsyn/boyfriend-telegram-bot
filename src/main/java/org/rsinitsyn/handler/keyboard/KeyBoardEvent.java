package org.rsinitsyn.handler.keyboard;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum KeyBoardEvent {
    // TODO How to translate to English?
    COMPLIMENT("Получить комплимент"),
    INSULT("Получить оскорбление");

    private final String text;

    KeyBoardEvent(String text) {
        this.text = text;
    }

    public static KeyBoardEvent ofText(String text) {
        return Arrays.stream(values())
                .filter(keyBoardEvent -> keyBoardEvent.text.equals(text))
                .findFirst()
                .orElse(null);
    }

    public static boolean hasText(String text) {
        return ofText(text) != null;
    }
}
