package org.rsinitsyn.handler.keyboard.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.dto.ComplimentCallbackDto;
import org.rsinitsyn.entity.RawCompliment;
import org.rsinitsyn.handler.keyboard.KeyBoardEvent;
import org.rsinitsyn.handler.keyboard.KeyBoardEventHandler;
import org.rsinitsyn.model.MessageWrapper;
import org.rsinitsyn.model.TelegramUserSession;
import org.rsinitsyn.service.ComplimentService;
import org.rsinitsyn.service.LocaleMessageService;
import org.rsinitsyn.utils.Emoji;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
@RequiredArgsConstructor
@Slf4j
public class ComplimentEventHandler implements KeyBoardEventHandler {

    private final ComplimentService complimentService;
    private final LocaleMessageService localeMessageService;
    private final ObjectMapper objectMapper;

    @Override
    public SendMessage handleEvent(MessageWrapper messageWrapper) {
        Message message = messageWrapper.getMessage();
        TelegramUserSession session = messageWrapper.getSession();

        RawCompliment complimentEntity = complimentService.get(message.getFrom());

        ComplimentCallbackDto likeCallbackDto = new ComplimentCallbackDto(
                "compliment_vote",
                complimentEntity.getId()
        );

        ComplimentCallbackDto dislikeCallbackDto = new ComplimentCallbackDto(
                "compliment_vote",
                complimentEntity.getId()
        );

        InlineKeyboardMarkup voteKeyboard = complimentVoteInlineKeyboardMarkup(likeCallbackDto, dislikeCallbackDto);

        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(localeMessageService.getMessage(
                        "template.compliment",
                        session.getLocale(),
                        complimentEntity.getText(),
                        Emoji.HEART.getValue()
                ))
                .replyMarkup(voteKeyboard)
                .build();
    }

    @SneakyThrows
    private InlineKeyboardMarkup complimentVoteInlineKeyboardMarkup(ComplimentCallbackDto yesDto, ComplimentCallbackDto noDto) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(simpleKeyBoardButton("Да", objectMapper.writeValueAsString(yesDto)));
        buttons.add(simpleKeyBoardButton("Нет", objectMapper.writeValueAsString(noDto)));

        return InlineKeyboardMarkup.builder()
                .keyboardRow(buttons)
                .build();
    }

    private InlineKeyboardButton simpleKeyBoardButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    @Override
    public KeyBoardEvent event() {
        return KeyBoardEvent.COMPLIMENT;
    }
}
