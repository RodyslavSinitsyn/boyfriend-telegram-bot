package org.rsinitsyn.handler.keyboard;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum KeyBoardEvent {
    // TODO How to translate to English?
    COMPLIMENT("Получить комплимент \uD83C\uDF38"),
    INSULT("Получить оскорбление \uD83E\uDD2C"),

    RANDOM_ACTION("Получить наугад...\uD83D\uDC40"),
    IMAGE("Картинку"),
    VIDEO("Видео"),
    AUDIO("Аудио"),
    DOCUMENT("Документ"),

    BACK_TO_MENU("Вернуться в меню");

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
