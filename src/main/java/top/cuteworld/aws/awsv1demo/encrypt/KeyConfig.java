package top.cuteworld.aws.awsv1demo.encrypt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyConfig {

    @Bean
    public KeyHandler keyHandler() {
        return new KeyHandlerTempImpl();
    }
}
