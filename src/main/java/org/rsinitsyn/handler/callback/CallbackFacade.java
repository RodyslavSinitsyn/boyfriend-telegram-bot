package org.rsinitsyn.handler.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.dto.ComplimentCallbackDto;
import org.rsinitsyn.model.TelegramUserSession;
import org.rsinitsyn.service.ComplimentService;
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

    @SneakyThrows
    public BotApiMethod<?> handleCallback(CallbackQuery callbackQuery, TelegramUserSession session) {
        Message message = callbackQuery.getMessage();
        String jsonData = callbackQuery.getData();

        ComplimentCallbackDto complimentCallbackDto = objectMapper.readValue(jsonData, ComplimentCallbackDto.class);

        complimentService.addLike(complimentCallbackDto.id());

        InlineKeyboardMarkup edited = complimentVoteInlineKeyboardMarkup(
                complimentCallbackDto,
                new ComplimentCallbackDto(complimentCallbackDto.cbId(), complimentCallbackDto.id())
        );

        return EditMessageReplyMarkup.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .replyMarkup(edited)
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
