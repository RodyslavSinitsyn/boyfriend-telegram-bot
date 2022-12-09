package org.rsinitsyn.facade;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rsinitsyn.handler.command.CommandHandler;
import org.rsinitsyn.service.LocaleMessageService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandFacade {

    private final List<CommandHandler> commandHandlers;
    private final LocaleMessageService messageService;

    public BotApiMethod<?> handle(Message message) {
        User user = message.getFrom();
        List<MessageEntity> entities = message.getEntities();

        Optional<MessageEntity> botCommand = entities.stream()
                .filter(entity -> entity.getType().equals("bot_command"))
                .findFirst();

        if (botCommand.isEmpty()) {
            log.warn("Not found entity 'bot_command'. {}", entities);
            return null;
        }
        MessageEntity messageEntity = botCommand.get();
        String command = messageEntity.getText();

        log.info("New [command] from username: {}, chatId: {}, text: {}",
                message.getFrom().getUserName(),
                message.getChat().getId(),
                command);

        CommandHandler commandHandler = getCommandHandler(command);

        if (commandHandler == null) {
            log.warn("Not found command handler, chatId: {}, text: {}",
                    message.getChat().getId(),
                    command);
            return SendMessage
                    .builder()
                    .chatId(user.getId())
                    .text(messageService.getMessage("reply.invalidCommand"))
                    .build();
        }

        return commandHandler.handleCommand(message);
    }

    private CommandHandler getCommandHandler(String command) {
        return commandHandlers.stream()
                .filter(commandHandler -> commandHandler.commandName().equals(command))
                .findFirst()
                .orElse(null);
    }
}