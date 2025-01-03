package tabom.myhands.common.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "dayoff")
public class DayOffProperties {
    private final String full;
    private final String morning;
    private final String afternoon;
    private final String fullPrefix;
}