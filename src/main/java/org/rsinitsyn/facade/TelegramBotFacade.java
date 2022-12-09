package org.rsinitsyn.facade;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.context.TelegramUserSession;
import org.rsinitsyn.context.UserSessionStorage;
import org.rsinitsyn.exception.EmptyMessageException;
import org.rsinitsyn.model.MessageWrapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

// Root Facade
@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramBotFacade {

    private final CommandFacade commandFacade;
    private final KeyBoardCommandFacade keyBoardCommandFacade;
    private final UserSessionStorage userSessionStorage;

    // telegram services
    private final ReplyKeyboardMarkup defaultReplyKeyboardMarkup;
    private final InlineKeyboardMarkup complimentVoteInlineKeyboardMarkup;


    // rsinitsyn - 538166938
    // fastysha - 408716263
    public BotApiMethod<?> handleUpdate(Update update) {
        log.info("-----------------------------------------------------");

        BotApiMethod<?> response = null;

        // whatever, just to log and do not throw an exception
        if (!update.hasCallbackQuery() && !update.hasMessage()) {
            log.debug("No callBack, no message, update: {}", update);
            return null;
        }

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
            TelegramUserSession userSession = userSessionStorage.getOrCreate(message.getChatId());
            response = commandFacade.handle(new MessageWrapper(
                    message,
                    userSession
            ));
        } else if (Objects.nonNull(message) && message.hasText()) {
            log.info("New [message] from username: {}, chatId: {}, text: {}",
                    message.getFrom().getUserName(),
                    message.getChat().getId(),
                    message.getText());
            response = keyBoardCommandFacade.handle(message);
        }

        // TODO Temp solution
        ((SendMessage)response).setReplyMarkup(defaultReplyKeyboardMarkup);

        return response;
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
