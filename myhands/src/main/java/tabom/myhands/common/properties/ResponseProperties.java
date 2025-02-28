package tabom.myhands.common.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;


@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "response")
public class ResponseProperties {

    private final String success;
    private final String fail;
}
