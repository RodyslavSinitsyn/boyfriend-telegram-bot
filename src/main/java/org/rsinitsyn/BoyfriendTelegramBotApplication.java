package org.rsinitsyn;

import org.rsinitsyn.props.BotProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(value = BotProperties.class)
@SpringBootApplication
public class BoyfriendTelegramBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoyfriendTelegramBotApplication.class, args);
    }
}
