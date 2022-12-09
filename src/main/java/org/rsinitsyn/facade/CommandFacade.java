package org.rsinitsyn.facade;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@Slf4j
public class CommandFacade {

    public BotApiMethod<?> handle(Message message) {
        User user = message.getFrom();
        List<MessageEntity> entities = message.getEntities();

        // TODO handle all entities
        MessageEntity entity = entities.get(0);

        log.info("Receive command. Username: {}. Entity: {}", user.getUserName(), entity);

        String commandText = entity.getText();

        String description = switch (commandText) {
            case "/start" -> "Привет, я парень бот! Ты можешь общаться со мной пока твой парень занят :)";
            case "/help" -> "Я могу сделать тебе комплимент или же оскорбить тебя. Остальные функции в процессе разработки";
            default -> "Прости, но у меня нету такой команды";
        };

        return SendMessage.builder()
                .text(description)
                .chatId(message.getChatId())
                .build();
    }
}