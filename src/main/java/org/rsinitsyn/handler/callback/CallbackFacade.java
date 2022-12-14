package org.rsinitsyn.handler.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.components.BotComponents;
import org.rsinitsyn.dto.AssetVoteDto;
import org.rsinitsyn.model.TelegramUserSession;
import org.rsinitsyn.props.ApiProperties;
import org.rsinitsyn.utils.Emoji;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
@RequiredArgsConstructor
@Slf4j
public class CallbackFacade {

    private final ApiProperties apiProperties;
    private final ObjectMapper objectMapper;

    // TODO Handle other callbacks
    @SneakyThrows
    public BotApiMethod<?> handleCallback(CallbackQuery callbackQuery, TelegramUserSession session) {
        Message message = callbackQuery.getMessage();

        if (message.getGame() != null) {
            return AnswerCallbackQuery
                    .builder()
                    .url(apiProperties.getGameApiUrl())
                    .callbackQueryId(callbackQuery.getId())
                    .build();
        }

        String jsonData = callbackQuery.getData();
        var assetVoteDto = objectMapper.readValue(jsonData, AssetVoteDto.class);

        log.info("New [callback] from username: {}, chatId: {}, callbackId: {}",
                message.getFrom().getUserName(),
                message.getChat().getId(),
                assetVoteDto.cbid());

        InlineKeyboardButton button = assetVoteDto.liked() ?
                BotComponents.voteInlineKeyboardButton(new AssetVoteDto("vote", false), "Нравится") :
                BotComponents.voteInlineKeyboardButton(new AssetVoteDto("vote", true), "Нравится" + Emoji.HEART.getValue());

        return EditMessageReplyMarkup.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .replyMarkup(BotComponents.complimentVoteInlineKeyboardMarkup(button))
                .build();
    }
}
