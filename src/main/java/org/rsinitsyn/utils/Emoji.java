package org.rsinitsyn.utils;

import com.vdurmont.emoji.EmojiParser;

public enum Emoji {
    HEART(":heart:"),
    ROSE(":rose:"),
    LAUGH(":joy:"),
    CHECK_DONE(":heavy_check_mark:");

    Emoji(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return EmojiParser.parseToUnicode(value);
    }
}
