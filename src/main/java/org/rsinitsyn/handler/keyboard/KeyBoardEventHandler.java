package org.rsinitsyn.handler.keyboard;

import org.rsinitsyn.model.MessageWrapper;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

public interface KeyBoardEventHandler<T extends PartialBotApiMethod<?>> {

    T handleEvent(MessageWrapper messageWrapper);

    KeyBoardEvent event();
}
