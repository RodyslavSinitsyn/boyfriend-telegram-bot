package org.rsinitsyn.handler.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.dto.ComplimentCallbackDto;
import org.rsinitsyn.entity.ComplimentGrade;
import org.rsinitsyn.model.TelegramUserSession;
import org.rsinitsyn.service.ComplimentService;
import org.rsinitsyn.utils.Emoji;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
@RequiredArgsConstructor
@Slf4j
public class CallbackFacade {

    private final ComplimentService complimentService;
    private final ObjectMapper objectMapper;

    // TODO Handle other callbacks
    @SneakyThrows
    public BotApiMethod<?> handleCallback(CallbackQuery callbackQuery, TelegramUserSession session) {
        Message message = callbackQuery.getMessage();
        String jsonData = callbackQuery.getData();

        ComplimentCallbackDto complimentCallbackDto = objectMapper.readValue(jsonData, ComplimentCallbackDto.class);

        log.debug("Compliment callback details, id: {}, grade: {}",
                complimentCallbackDto.id(), complimentCallbackDto.g());
        ComplimentGrade complimentGrade = ComplimentGrade.valueOf(complimentCallbackDto.g());
        complimentService.updateGrade(message.getChatId(), complimentCallbackDto.id(), complimentGrade);

        InlineKeyboardMarkup existingMarkup = callbackQuery.getMessage().getReplyMarkup();

        InlineKeyboardButton buttonToUpdate = existingMarkup.getKeyboard().stream()
                .flatMap(Collection::stream)
                .filter(btn -> btn.getText().equals(complimentGrade.name()))
                .findFirst().orElseThrow();
        buttonToUpdate.setText(buttonToUpdate.getText() + Emoji.CHECK_DONE);

        return EditMessageReplyMarkup.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .replyMarkup(existingMarkup)
                .build();
    }

    // TODO Reuse this somehow not copy-past
    @SneakyThrows
    private InlineKeyboardMarkup complimentVoteInlineKeyboardMarkup(ComplimentCallbackDto yesDto, ComplimentCallbackDto noDto) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(simpleKeyBoardButton("Да (V)", objectMapper.writeValueAsString(yesDto)));
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
}
