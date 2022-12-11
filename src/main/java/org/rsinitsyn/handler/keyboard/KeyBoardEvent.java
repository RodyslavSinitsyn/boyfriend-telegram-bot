package org.rsinitsyn.handler.keyboard;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum KeyBoardEvent {
    // TODO How to translate to English?
    COMPLIMENT("Хочу комплимент \uD83C\uDF38"),
    JOKE("Хочу прикол \uD83D\uDE04"),
    GAME("Играть в игру \uD83C\uDFB2"),

    RANDOM_ACTION("Еще контента \uD83D\uDC40"),
    IMAGE("Картинку"),
    GIF("Гифку"),
    AUDIO("Аудио"),
    DOCUMENT("Документ"),

    BACK_TO_MENU("⬅️Меню");

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
