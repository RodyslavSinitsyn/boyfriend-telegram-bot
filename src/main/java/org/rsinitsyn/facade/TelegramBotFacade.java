package org.rsinitsyn.facade;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.exception.EmptyMessageException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramBotFacade {

    private final CommandFacade commandFacade;
    private final KeyBoardCommandFacade keyBoardCommandFacade;

    // telegram services
    private final ReplyKeyboardMarkup defaultReplyKeyboardMarkup;
    private final InlineKeyboardMarkup complimentVoteInlineKeyboardMarkup;


    public BotApiMethod<?> handleUpdate(Update update) {
        log.info("-----------------------------------------------------");
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New [callback] from username: {}, chatId: {}, callbackId: {}",
                    callbackQuery.getFrom().getUserName(),
                    callbackQuery.getMessage().getChat().getId(),
                    // TODO Verify what is this ID
                    callbackQuery.getId());
            return null;
        }
        Message message = update.getMessage();
        if (Objects.nonNull(message) && message.isCommand()) {
            // TODO handle command
            log.info("New [command] from username: {}, chatId: {}, text: {}",
                    message.getFrom().getUserName(),
                    message.getChat().getId(),
                    message.getText());
            return commandFacade.handle(message);
        }

        if (Objects.nonNull(message) && message.hasText()) {
            log.info("New [message] from username: {}, chatId: {}, text: {}",
                    message.getFrom().getUserName(),
                    message.getChat().getId(),
                    message.getText());
            return keyBoardCommandFacade.handle(message);
        }

        return null;
    }

    // TODO Remove?
    private void verifyMessage(Update update) {
        Message message = Optional.of(update)
                .map(Update::getMessage)
                .orElseThrow(() -> new EmptyMessageException("Message is null"));

        if (!message.hasText()) {
            log.warn("Message does not contain any text. ChatId: {}. Telegram user: {}", message.getChat().getId(), message.getChat().getUserName());
            throw new EmptyMessageException("Message text is empty");
        }
    }
}
