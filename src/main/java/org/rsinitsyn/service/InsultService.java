package org.rsinitsyn.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class InsultService {

    public String get(User user) {
        return user.getFirstName() + ", Я пока не умею оскорблять, да и надо ли это такой красотке как ты? Лучше воспользуйся комплиментом.";
    }
}
