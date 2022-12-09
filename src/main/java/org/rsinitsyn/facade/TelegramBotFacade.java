package org.rsinitsyn.facade;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.components.KeyBoardComponents;
import org.rsinitsyn.exception.EmptyMessageException;
import org.rsinitsyn.handler.command.CommandFacade;
import org.rsinitsyn.handler.keyboard.KeyBoardEvent;
import org.rsinitsyn.handler.keyboard.KeyBoardEventFacade;
import org.rsinitsyn.model.MessageWrapper;
import org.rsinitsyn.model.TelegramUserSession;
import org.rsinitsyn.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

// Root Facade
@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramBotFacade {

    private final CommandFacade commandFacade;
    private final KeyBoardEventFacade keyBoardEventFacade;
    private final UserSessionService userSessionService;

    // rsinitsyn - 538166938
    // fastysha - 408716263
    public BotApiMethod<?> handleUpdate(Update update) {
        log.info("-----------------------------------------------------");

        BotApiMethod<?> response;

        // whatever, just to log and do not throw an exception
        if (!update.hasCallbackQuery() && !update.hasMessage()) {
            log.debug("No callBack, no message, update: {}", update);
            return null;
        }

        Message message = update.getMessage();
        if (Objects.isNull(message)) {
            throw new EmptyMessageException("Message is null");
        }

        TelegramUserSession userSession = userSessionService.getOrCreate(message.getChatId());
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New [callback] from username: {}, chatId: {}, callbackId: {}",
                    callbackQuery.getFrom().getUserName(),
                    callbackQuery.getMessage().getChat().getId(),
                    // TODO Verify what is this ID
                    callbackQuery.getId());
            return null;
        }

        if (message.isCommand()) {
            response = commandFacade.handle(new MessageWrapper(
                    message,
                    userSession
            ));
        } else if (message.hasText() &&
                KeyBoardEvent.hasText(message.getText())) {
            response = keyBoardEventFacade.handle(new MessageWrapper(
                    message,
                    userSession
            ));
        } else {
            // other user input
            log.info("New [message] from username: {}, chatId: {}, text: {}",
                    message.getFrom().getUserName(),
                    message.getChat().getId(),
                    message.getText());
            response = SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text("Ку-ку")
                    .build();
        }

        ((SendMessage)response).setReplyMarkup(KeyBoardComponents.mainMenuKeyboardMarkup());

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
