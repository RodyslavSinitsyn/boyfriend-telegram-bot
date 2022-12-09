package org.rsinitsyn.handler.keyboard;

import org.rsinitsyn.model.MessageWrapper;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface KeyBoardEventHandler {

    SendMessage handleEvent(MessageWrapper messageWrapper);

    KeyBoardEvent event();
}
