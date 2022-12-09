package org.rsinitsyn.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot")
@Data
public class BotProperties {
    private String name;
    private String token;
    private Boolean notifyOnStartup = Boolean.FALSE;
    private Boolean notifyOnShutdown = Boolean.FALSE;
}