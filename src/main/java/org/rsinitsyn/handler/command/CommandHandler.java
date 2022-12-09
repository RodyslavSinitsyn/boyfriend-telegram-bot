package org.rsinitsyn.handler.command;

import org.rsinitsyn.model.MessageWrapper;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface CommandHandler {

    SendMessage handleCommand(MessageWrapper messageWrapper);

    String description();

    String commandName();
}
