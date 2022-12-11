package org.rsinitsyn.facade;

import java.util.Collection;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.components.BotComponents;
import org.rsinitsyn.exception.EmptyMessageException;
import org.rsinitsyn.handler.callback.CallbackFacade;
import org.rsinitsyn.handler.command.CommandFacade;
import org.rsinitsyn.handler.keyboard.KeyBoardEvent;
import org.rsinitsyn.handler.keyboard.KeyBoardEventFacade;
import org.rsinitsyn.model.MessageWrapper;
import org.rsinitsyn.model.TelegramUserSession;
import org.rsinitsyn.repository.TelegramUserRepository;
import org.rsinitsyn.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
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
    private final CallbackFacade callbackFacade;

    private final UserSessionService userSessionService;
    private final TelegramUserRepository telegramUserRepository;

    // rsinitsyn - 538166938
    // fastysha - 408716263
    public PartialBotApiMethod<?> handleUpdate(Update update) {
        log.info("-----------------------------------------------------");

        PartialBotApiMethod<?> response;

        // whatever, just to log and do not throw an exception
        if (!update.hasCallbackQuery() && !update.hasMessage()) {
            log.debug("No callBack, no message, update: {}", update);
            return null;
        }

        TelegramUserSession userSession = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            userSession = userSessionService.getOrCreate(
                    callbackQuery.getMessage().getChatId(),
                    callbackQuery.getMessage().getChat().getUserName());
            return callbackFacade.handleCallback(callbackQuery, userSession);
        }

        Message message = update.getMessage();
        if (Objects.isNull(message)) {
            throw new EmptyMessageException("Message is null");
        }

        userSession = userSessionService.getOrCreate(
                message.getChatId(),
                message.getChat().getUserName());


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
                    .replyMarkup(BotComponents.mainMenuKeyboardMarkup())
                    .build();
        }

        return response;
    }

    public Collection<SendMessage> getNotificationMessagesOnStartup() {
        return telegramUserRepository.findAll()
                .stream()
                .map(telegramUser -> SendMessage.builder()
                        .chatId(telegramUser.getChatId())
                        .text("Я проснулся и готов с тобой пообщаться")
                        .build()
                )
                .toList();
    }

    public Collection<SendMessage> getNotificationMessagesOnShutdown() {
        return telegramUserRepository.findAll()
                .stream()
                .map(telegramUser -> SendMessage.builder()
                        .chatId(telegramUser.getChatId())
                        .text("Я пока отдохну, спишемся позже :)")
                        .disableNotification(true)
                        .build()
                )
                .toList();
    }
}
