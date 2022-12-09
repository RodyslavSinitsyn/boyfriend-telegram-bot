package org.rsinitsyn.bot;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.api.BoyfriendBotApi;
import org.rsinitsyn.exception.EmptyMessageException;
import org.rsinitsyn.handler.BaseCommandHandler;
import org.rsinitsyn.handler.KeyBoardCommandHandler;
import org.rsinitsyn.props.BotProperties;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoyfriendBot extends TelegramLongPollingBot implements BoyfriendBotApi {

    // business services
    private final BotProperties botProperties;
    private final BaseCommandHandler baseCommandHandler;
    private final KeyBoardCommandHandler keyBoardCommandHandler;

    // telegram services
    private final ReplyKeyboardMarkup defaultReplyKeyboardMarkup;
    private final InlineKeyboardMarkup complimentVoteInlineKeyboardMarkup;

    @Override
    public void handleUpdate(Update update) {
        log.info("-----------------------------------------");
        verifyMessage(update);
        rootMessageHandler(update.getMessage());
    }

    private void rootMessageHandler(Message message) {
        Chat chat = message.getChat();
        String text = message.getText();
        log.info("Bot got message from user: {}, text: {}", chat.getUserName(), text);

        if (message.isCommand()) {
            String commandResponse = baseCommandHandler.getResponseByCommand(message);
            sendTextMessageBack(chat.getId(), commandResponse);
            return;
        }
        // try to handle keyboard commands
        Optional<String> responseByText = keyBoardCommandHandler.getResponseByText(message);
        if (responseByText.isPresent()) {
            sendTextMessageBack(chat.getId(), responseByText.get());
        } else {
            // default for now
            sendTextMessageBack(chat.getId(), "Даже и не знаю что тебе ответить " + chat.getFirstName());
        }
    }

    private void sendTextMessageBack(Long chatId, String text) {
        SendMessage textMessage = new SendMessage();
        textMessage.setChatId(idToStr(chatId));
        textMessage.setText(text);
        textMessage.setReplyMarkup(defaultReplyKeyboardMarkup);
        textMessage.setReplyMarkup(complimentVoteInlineKeyboardMarkup);

        sendAnyMessage(textMessage);
    }

    private void sendAnyMessage(BotApiMethodMessage method) {
        try {
            Message response = execute(method);
            log.info("Message has been sent. Receiver username: {}", response.getChat().getUserName());
        } catch (TelegramApiException e) {
            log.error("Message has not been sent.", e);
        }
    }

    private String idToStr(Long id) {
        return Long.toString(id);
    }

    private void verifyMessage(Update update) {
        Message message = Optional.of(update)
                .map(Update::getMessage)
                .orElseThrow(() -> new EmptyMessageException("Message is null"));

        if (!message.hasText()) {
            log.warn("Message does not contain any text. ChatId: {}. Telegram user: {}", message.getChat().getId(), message.getChat().getUserName());
            throw new EmptyMessageException("Message text is empty");
        }
    }

    @Override
    public void notifyOnInit() {

    }

    @Override
    public void notifyOnShutdown() {

    }

    @Override
    public void onUpdateReceived(Update update) {
        // delegate request to process business logic
        handleUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }
}
