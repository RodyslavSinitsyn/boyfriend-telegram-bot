package org.rsinitsyn.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot")
@Data
public class BotProperties {
    private String name;
    private String token;
    private boolean notifyOnStartup;
    private boolean notifyOnShutdown;
    private String defaultLocale;
}