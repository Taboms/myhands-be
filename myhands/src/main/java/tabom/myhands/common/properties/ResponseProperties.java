package tabom.myhands.common.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;


@Getter
@ConfigurationProperties(prefix = "response")
public class ResponseProperties {

    private final String success;
    private final String fail;

    @ConstructorBinding
    public ResponseProperties(String success, String fail) {
        this.success = success;
        this.fail = fail;
    }
}
