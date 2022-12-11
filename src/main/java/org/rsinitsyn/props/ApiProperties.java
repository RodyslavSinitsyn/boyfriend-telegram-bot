package org.rsinitsyn.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api")
@Getter
@Setter
public class ApiProperties {

    private String complimentApiUrl;
    private String jokeApiUrl;
    private String gameApiUrl;
}
