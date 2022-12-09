package org.rsinitsyn.repository;

import java.util.Optional;
import java.util.UUID;
import org.rsinitsyn.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, UUID> {

    Optional<TelegramUser> findByChatId(Long chatId);
}
