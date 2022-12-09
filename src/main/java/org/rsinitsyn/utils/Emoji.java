package org.rsinitsyn.utils;

import com.vdurmont.emoji.EmojiParser;

public enum Emoji {
    HEART(":heart:"),
    ROSE(":rose:");

    Emoji(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return EmojiParser.parseToUnicode(value);
    }
}
