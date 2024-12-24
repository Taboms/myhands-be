package tabom.myhands;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tabom.myhands.common.properties.ResponseProperties;

@SpringBootApplication
@EnableConfigurationProperties(ResponseProperties.class)
public class MyhandsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyhandsApplication.class, args);
	}

}
