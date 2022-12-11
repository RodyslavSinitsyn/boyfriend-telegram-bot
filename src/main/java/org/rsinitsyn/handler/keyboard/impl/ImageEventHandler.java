package org.rsinitsyn.handler.keyboard.impl;

import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.rsinitsyn.components.BotComponents;
import org.rsinitsyn.handler.keyboard.KeyBoardEvent;
import org.rsinitsyn.handler.keyboard.KeyBoardEventHandler;
import org.rsinitsyn.model.MessageWrapper;
import org.rsinitsyn.service.LocaleMessageService;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class ImageEventHandler implements KeyBoardEventHandler<SendPhoto> {

    private final LocaleMessageService localeMessageService;

    @SneakyThrows
    @Override
    public SendPhoto handleEvent(MessageWrapper messageWrapper) {
        File file = ResourceUtils.getFile("src/main/resources/files/Doggy.png");

        Message message = messageWrapper.getMessage();
        return SendPhoto.builder()
                .chatId(message.getChatId())
                .caption(localeMessageService.getMessage("asset.imageCaption", messageWrapper.getSession().getLocale()))
                .photo(new InputFile(file))
                .protectContent(true)
                .replyMarkup(BotComponents.complimentVoteInlineKeyboardMarkup())
                .build();
    }

    @Override
    public KeyBoardEvent event() {
        return KeyBoardEvent.IMAGE;
    }
}
