package org.rsinitsyn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "chatId")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tg_user")
public class TelegramUser {
    @Id
    private Long chatId; // eq to telegramUserId
    @Column(unique = true, nullable = false)
    private String username;
    private LocalDateTime registrationDate;
}
