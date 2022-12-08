package org.rsinitsyn;

import org.rsinitsyn.props.BotProperties;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(value = BotProperties.class)
@SpringBootApplication
public class BoyfriendTelegramBotApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BoyfriendTelegramBotApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
