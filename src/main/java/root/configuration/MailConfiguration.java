package root.configuration;

import root.service.MailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfiguration {
    @Bean
    public MailService mailService() {
        return new MailService();
    }
}
