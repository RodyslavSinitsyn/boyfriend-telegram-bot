package org.rsinitsyn.utils;

import com.vdurmont.emoji.EmojiParser;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BotUtils {

    private static final String GENERIC_TEMPLATE = "%s%s";

    /*
    Adds emoji at the end.
     */
    public static String addEmoji(String text, String... emojiShortCodes) {
        String emojiesSpaceSeparated = Arrays.stream(emojiShortCodes)
                .map(EmojiParser::parseToUnicode)
                .collect(Collectors.joining(" "));

        return useTemplate(text, emojiesSpaceSeparated);
    }

    private static String useTemplate(String baseText, String emojiText) {
        return String.format(GENERIC_TEMPLATE, baseText, emojiText);
    }
}
