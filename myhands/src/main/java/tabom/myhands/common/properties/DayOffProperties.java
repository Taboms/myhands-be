package tabom.myhands.common.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "dayoff")
public class DayOffProperties {
    private final String redisKeyPrefix;
    private final String full;
    private final String fullPrefix;
    private final String morning;
    private final String morningPrefix;
    private final String afternoon;
    private final String afternoonPrefix;
    private final String dayOffCnt;
}
