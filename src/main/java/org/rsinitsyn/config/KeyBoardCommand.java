package org.rsinitsyn.config;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum KeyBoardCommand {
    COMPLIMENT("Получить комплимент"),
    INSULT("Получить оскорбление"),
    // to make sure nobody input smth like this
    DEFAULT("971b4db3-4ba3-4f5f-a597-6a4cc3ded4f4");

    private final String text;

    KeyBoardCommand(String text) {
        this.text = text;
    }

    public static KeyBoardCommand ofText(String text) {
        return Arrays.stream(values())
                .filter(keyBoardCommand -> keyBoardCommand.text.equals(text))
                .findFirst()
                .orElse(DEFAULT);
    }
}
